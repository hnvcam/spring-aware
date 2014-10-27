package org.thirdparty.impl;

import com.aiwsolutions.springaware.bundle.ExportService;
import org.springframework.stereotype.Service;
import org.thirdparty.OtherService;

/**
 * Created by camhoang on 10/28/14.
 */
@ExportService(OtherService.class)
@Service
public class OtherServiceImpl implements OtherService {
    @Override
    public boolean isService() {
        return true;
    }
}
