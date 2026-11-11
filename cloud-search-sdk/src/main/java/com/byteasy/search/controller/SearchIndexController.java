package com.byteasy.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.search.service.SearchIndexService;
import com.byteasy.util.RequestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/search")
@Log4j2
public class SearchIndexController {

    @Autowired
    private SearchIndexService searchIndexService;

    @GetMapping("")
    @Openapi
    public Result search(@RequestParam("keyword") String keyword, @RequestParam(required = false, value = "className") String className) {
        Page page = RequestUtil.getPage();
        return Result.success(searchIndexService.search(keyword, className, page.getCurrent(), page.getSize()));
    }

}
