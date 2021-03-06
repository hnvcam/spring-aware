package com.aiwsolutions.springaware;

import com.aiwsolutions.springaware.bundle.BundleService;
import com.aiwsolutions.springaware.bundle.ExportService;
import com.aiwsolutions.springaware.dm.ServiceExposer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by camhoang on 9/21/14.
 */
public class SpringAwareBundleActivator implements BundleActivator, Runnable {
    private static final Logger log = Logger.getLogger(SpringAwareBundleActivator.class.getName());

    private static volatile BundleContext bundleContext;
    private GenericApplicationContext applicationContext;
    private ServiceExposer serviceExposer;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        initContextClassLoader();
        SpringAwareBundleActivator.bundleContext = bundleContext;
        this.serviceExposer = new ServiceExposer(bundleContext);
        new Thread(this).run();
    }

    private void initContextClassLoader() throws ClassNotFoundException {
        Class clazz = Class.forName("org.springframework.context.support.GenericApplicationContext");
        Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        serviceExposer.unregisterServices();
        applicationContext.stop();
    }

    @Override
    public void run() {

        applicationContext = new GenericApplicationContext() {
            @Override
            protected ResourcePatternResolver getResourcePatternResolver() {
                return new BundleResourcePatternResolver(SpringAwareBundleActivator.bundleContext.getBundle());
            }
        };
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(applicationContext);
        xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
        xmlReader.loadBeanDefinitions(new BundleResource("springAware-context.xml"));
        applicationContext.setClassLoader(this.getClass().getClassLoader());
        applicationContext.refresh();

        Map<String, BundleService> bundleServiceMap = applicationContext.getBeansOfType(BundleService.class);

        for (Map.Entry<String, BundleService> entry : bundleServiceMap.entrySet()) {
            serviceExposer.expose(entry.getValue());
        }

        Map<String, Object> exportServiceMap = applicationContext.getBeansWithAnnotation(ExportService.class);

        for (Map.Entry<String, Object> entry : exportServiceMap.entrySet()) {
            serviceExposer.exposeAnnotationService(entry.getValue());
        }
    }

    public static final BundleContext getBundleContext() {
        return SpringAwareBundleActivator.bundleContext;
    }
}
