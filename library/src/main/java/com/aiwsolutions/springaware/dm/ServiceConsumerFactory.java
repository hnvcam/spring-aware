package com.aiwsolutions.springaware.dm;

import com.aiwsolutions.springaware.SpringAwareBundleActivator;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by camhoang on 9/25/14.
 */
public class ServiceConsumerFactory implements FactoryBean {
    private Class serviceInterface;

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Class getServiceInterface() {
        return serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        ServiceReference serviceReference = SpringAwareBundleActivator.getBundleContext().getServiceReference(serviceInterface.getName());
        if (serviceReference != null) {
            return SpringAwareBundleActivator.getBundleContext().getService(serviceReference);
        }
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
