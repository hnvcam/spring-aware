package com.aiws.springaware.test.impl;

import com.aiws.springaware.bundle.BundleService;
import com.aiws.springaware.test.SampleService;
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
