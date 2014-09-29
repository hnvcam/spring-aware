package com.aiwsolutions.springaware;

import org.osgi.framework.Bundle;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by camhoang on 9/24/14.
 */
public class BundleResourcePatternResolver implements ResourcePatternResolver {
    private static final Logger log = Logger.getLogger(BundleResourcePatternResolver.class.getName());
    private static final String CLASS_PATTERN = "*.class";

    private volatile Bundle thisBundle;
    private PathMatcher pathMatcher;

    public BundleResourcePatternResolver(Bundle bundle) {
        thisBundle = bundle;
        pathMatcher = new AntPathMatcher();
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        log.info("getResources for pattern: " + locationPattern);
        if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            // a class path resource (multiple resources for same name possible)
            if (pathMatcher.isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
                // a class path resource pattern
                return findPathMatchingResources(locationPattern);
            }
            else {
                // all class path resources with the given name
                return findAllClassPathResources(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            }
        }
        else {
            // Only look for a pattern after a prefix here
            // (to not get fooled by a pattern symbol in a strange prefix).
            int prefixEnd = locationPattern.indexOf(":") + 1;
            if (pathMatcher.isPattern(locationPattern.substring(prefixEnd))) {
                // a file pattern
                return findPathMatchingResources(locationPattern);
            }
            else {
                // a single resource with the given name
                return new Resource[] {getResource(locationPattern)};
            }
        }
    }

    private Resource[] findAllClassPathResources(String location) {
        log.info("findAllClassPathResources for location: " + location);
        Set<Resource> result = new LinkedHashSet<>();
        Enumeration<URL> e = thisBundle.findEntries(location, CLASS_PATTERN, true);
        while (e.hasMoreElements()) {
            result.add(new UrlResource(e.nextElement()));
        }
        return result.toArray(new Resource[result.size()]);
    }

    private Resource[] findPathMatchingResources(String locationPattern) throws IOException {
        log.info("findPathMatchingResources for locationPattern: " + locationPattern);
        String rootDirPath = determineRootDir(locationPattern);
        return getResources(rootDirPath);
    }

    @Override
    public Resource getResource(String location) {
        return new UrlResource(thisBundle.getEntry(location));
    }

    @Override
    public ClassLoader getClassLoader() {
        return thisBundle.getClass().getClassLoader();
    }

    private String determineRootDir(String location) {
        int prefixEnd = location.indexOf(":") + 1;
        int rootDirEnd = location.length();
        while (rootDirEnd > prefixEnd && pathMatcher.isPattern(location.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return location.substring(0, rootDirEnd);
    }
}
