package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;
import java.util.Map;

public interface IBaseEntityService<ID, ENTITY> {

    @GetExchange("")
    Result<Page<ENTITY>> page(@RequestParam Map<String, Object> params);

    @GetExchange("/list/match")
    Result<List<ENTITY>> list(@RequestParam Map<String, Object> params);

    @GetExchange("/{id}")
    Result<ENTITY> getById(@PathVariable("id") ID id);

    @PostExchange("")
    Result<ENTITY> saveOrUpdate(@RequestBody ENTITY entity);

    @PostExchange("/batch")
    Result<List<ENTITY>> saveOrUpdateBatch(@RequestBody List<ENTITY> entities);

    @PutExchange("")
    Result<ENTITY> updateById(@RequestBody ENTITY entity);

    @PutExchange("/batch")
    Result<List<ENTITY>> updateBatchById(@RequestBody List<ENTITY> entities);

    @DeleteExchange("/{id}")
    Result<Boolean> removeById(@PathVariable("id") ID id);

    @DeleteExchange("")
    Result<Boolean> removeByWrapper(@RequestBody ENTITY entity);

}

