package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public class IPlatformCommonServiceFallback implements IPlatformCommonService {

    @Override
    public Result<Map<String, Object>> getById(String className, Object id) {
        return null;
    }

    @Override
    public Result<Page<Map<String, Object>>> page(String className, Map<String, Object> queryParams) {
        return null;
    }

    @Override
    public Result<List<Map<String, Object>>> list(String className, Map<String, Object> queryParams) {
        return null;
    }
}
