package com.aiwsolutions.springaware;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by camhoang on 10/27/14.
 */
public class BundleResource extends AbstractResource {
    private String entry;

    public BundleResource(String entry) {
        this.entry = entry;
    }

    @Override
    public String getDescription() {
        return "SpringAware Bundle Resource";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getURL().openStream();
    }

    @Override
    public URL getURL() throws IOException {
        return SpringAwareBundleActivator.getBundleContext().getBundle().getEntry(this.entry);
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return new BundleResource(relativePath);
    }

    @Override
    public int hashCode() {
        return entry.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BundleResource) {
            return entry.equals(((BundleResource) obj).entry);
        }
        return false;
    }
}
