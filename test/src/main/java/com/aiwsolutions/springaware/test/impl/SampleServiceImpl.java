package com.aiwsolutions.springaware.test.impl;

import com.aiwsolutions.springaware.bundle.BundleService;
import com.aiwsolutions.springaware.test.SampleService;
import org.springframework.stereotype.Service;

/**
 * Created by camhoang on 9/24/14.
 */
@Service
public class SampleServiceImpl implements SampleService, BundleService {
    @Override
    public Class getServiceInterface() {
        return SampleService.class;
    }

    @Override
    public String helloWorld() {
        return "hello world";
    }
}
