package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface IBaseEntityService<ID, ENTITY> {

    @GetMapping("")
    Result<Page<ENTITY>> page(@RequestParam Map<String, Object> params);

    @GetMapping("/list/match")
    Result<List<ENTITY>> list(@RequestParam Map<String, Object> params);

    @GetMapping("/{id}")
    Result<ENTITY> getById(@PathVariable("id") ID id);

    @PostMapping("")
    Result<ENTITY> saveOrUpdate(@RequestBody ENTITY entity);

    @PostMapping("/batch")
    Result<List<ENTITY>> saveOrUpdateBatch(@RequestBody List<ENTITY> entities);

    @PutMapping("")
    Result<ENTITY> updateById(@RequestBody ENTITY entity);

    @PutMapping("/batch")
    Result<List<ENTITY>> updateBatchById(@RequestBody List<ENTITY> entities);

    @DeleteMapping("/{id}")
    Result<Boolean> removeById(@PathVariable("id") ID id);

    @DeleteMapping("")
    Result<Boolean> removeByWrapper(@RequestBody ENTITY entity);

}

