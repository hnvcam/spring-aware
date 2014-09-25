package com.aiws.springaware.dm;

import com.aiws.springaware.bundle.BundleService;
import com.aiws.springaware.exception.InvalidBundleServiceException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.ArrayList;
import java.util.List;
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

        ServiceRegistration reg = bundleContext.registerService(serviceInterface,
                bean,
                null);
        serviceMap.put(bean.getClass().getName(), reg);
    }

    public void unregisterServices() {
        for (Map.Entry<String, ServiceRegistration> entry : serviceMap.entrySet()) {
            entry.getValue().unregister();
        }
    }
}
