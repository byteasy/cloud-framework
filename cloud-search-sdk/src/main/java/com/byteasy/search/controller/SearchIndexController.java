package com.byteasy.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.search.dto.SearchQueryDTO;
import com.byteasy.search.dto.SearchResultVO;
import com.byteasy.search.service.SearchIndexService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/search")
@Log4j2
public class SearchIndexController {

    @Autowired
    private SearchIndexService searchIndexService;

    @PostMapping("")
    @Openapi
    public Result<Page<SearchResultVO>> search(@RequestBody SearchQueryDTO searchQueryDTO) {
        return Result.success(searchIndexService.search(searchQueryDTO));
    }

}
