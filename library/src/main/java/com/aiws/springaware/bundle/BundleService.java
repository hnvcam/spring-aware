package com.aiws.springaware.bundle;
/**
 * Created by camhoang on 9/21/14.
 *
 * This interface is to indicate that a Spring service is to expose as OSGi services.
 *
 */
public interface BundleService {
    Class getServiceInterface();
}
