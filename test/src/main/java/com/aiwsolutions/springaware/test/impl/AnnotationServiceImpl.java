package com.aiwsolutions.springaware.test.impl;

import com.aiwsolutions.springaware.bundle.ExportService;
import com.aiwsolutions.springaware.test.AnnotationService;
import org.springframework.stereotype.Service;

/**
 * Created by camhoang on 10/1/14.
 */
@Service
@ExportService(AnnotationService.class)
public class AnnotationServiceImpl implements AnnotationService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
