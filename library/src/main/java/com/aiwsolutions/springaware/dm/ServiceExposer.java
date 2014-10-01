package com.aiwsolutions.springaware.dm;

import com.aiwsolutions.springaware.bundle.BundleService;
import com.aiwsolutions.springaware.bundle.ExportService;
import com.aiwsolutions.springaware.exception.InvalidBundleServiceException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by camhoang on 9/22/14.
 */
public class ServiceExposer {

    private final BundleContext bundleContext;
    private volatile ConcurrentHashMap<String, ServiceRegistration> serviceMap;

    public ServiceExposer(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.serviceMap = new ConcurrentHashMap<>();
    }

    public void expose(BundleService bean) {
        Class[] implementedInterfaces = bean.getClass().getInterfaces();
        String serviceInterface = null;

        for (Class inf : implementedInterfaces) {
            if (inf.getName().equals(bean.getServiceInterface().getName()))
                serviceInterface = inf.getName();
        }
        if (serviceInterface == null)
            throw new InvalidBundleServiceException("Bean does not implement a service interface");

        registerService(serviceInterface, bean, null);
    }

    public void unregisterServices() {
        for (Map.Entry<String, ServiceRegistration> entry : serviceMap.entrySet()) {
            entry.getValue().unregister();
        }
    }

    public void exposeAnnotationService(Object value) {
        ExportService serviceDescription = value.getClass().getAnnotation(ExportService.class);
        if (serviceDescription != null) {
            registerService(serviceDescription.value().getName(), value, null);
        }
    }

    private void registerService(String interfaceName, Object bean, Dictionary properties) {
        ServiceRegistration reg = bundleContext.registerService(interfaceName,
                bean,
                properties);
        serviceMap.put(bean.getClass().getName(), reg);
    }
}
