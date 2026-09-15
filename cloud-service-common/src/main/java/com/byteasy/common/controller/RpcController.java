package com.byteasy.common.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.common.util.FieldDefineUtil;
import com.byteasy.exception.BizException;
import com.byteasy.util.I18nUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.List;


@Log4j2
public abstract class RpcController<SERVICE extends IService<ENTITY>, ENTITY, ID extends Serializable> extends AbstractController<SERVICE, ENTITY, ID> {

    @GetMapping("")
    @Openapi
    @Operation(summary = "分页查询")
    public Page<ENTITY> page() throws Exception {
        return baseService.page(RequestUtil.getPage(), RequestUtil.getQueryWrapper(baseService.getEntityClass()));
    }

    @GetMapping("/{id}")
    @Openapi
    @Operation(summary = "根据主键查找")
    @Parameters({@Parameter(name = "id", description = "主键")})
    public ENTITY getById(@PathVariable("id") ID id) throws Exception {
        return baseService.getById(id);
    }

    @PostMapping("/ids")
    @Openapi
    @Operation(summary = "根据主键查找")
    @Parameters({@Parameter(name = "id", description = "主键")})
    public List<ENTITY> listByIds(@RequestBody List<ID> ids) throws Exception {
        return baseService.listByIds(ids);
    }

    @DeleteMapping("/ids")
    @Openapi
    @Operation(summary = "根据主键查找")
    @Parameters({@Parameter(name = "id", description = "主键")})
    public boolean removeBatchByIds(@RequestBody List<ID> ids) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeBatchByIds: {}", JacksonUtil.toJson(ids));
        }
        return baseService.removeBatchByIds(ids);
    }

    @PostMapping("")
    @Openapi
    @Operation(summary = "存在更新记录，否则插入一条记录")
    public ENTITY saveOrUpdate(@RequestBody ENTITY entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdate: {}", JacksonUtil.toJson(entity));
        }
        if (!baseService.saveOrUpdate(entity)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return entity;
    }

    @PostMapping("/batch")
    @Openapi
    @Operation(summary = "存在更新记录，否则插入记录（批量）")
    public List<ENTITY> saveOrUpdateBatch(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdateBatch: {}", JacksonUtil.toJson(entities));
        }
        if (!baseService.saveOrUpdateBatch(entities)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return entities;
    }

    @PutMapping("")
    @Openapi
    @Operation(summary = "根据主键更新记录")
    public ENTITY updateById(@RequestBody ENTITY entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("updateById: {}", JacksonUtil.toJson(entity));
        }
        if (!baseService.updateById(entity)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return entity;
    }

    @PutMapping("/batch")
    @Openapi
    @Operation(summary = "根据主键批量更新记录")
    public List<ENTITY> updateBatchById(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("updateBatchById: {}", JacksonUtil.toJson(entities));
        }
        if (!baseService.updateBatchById(entities)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return entities;
    }

    @DeleteMapping("/{id}")
    @Openapi
    @Operation(summary = "根据主键删除记录")
    public Boolean removeById(@PathVariable("id") ID id) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeById: {}", id);
        }
        return baseService.removeById(id);
    }

    @DeleteMapping("/batch")
    @Openapi
    @Operation(summary = "根据主键批量删除记录")
    public Result<Boolean> removeByIds(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeByIds: {}", JacksonUtil.toJson(entities));
        }
        return Result.success(baseService.removeByIds(entities));
    }

    @DeleteMapping("")
    @Openapi
    @Operation(summary = "根据条件批量删除记录（慎重）")
    public Result<Boolean> removeByWrapper(@RequestBody ENTITY entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeByWrapper: {}", JacksonUtil.toJson(entity));
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String contentType = request.getHeader("content-type");
        if (StringUtils.isEmpty(contentType)) {
            return Result.success(baseService.remove(RequestUtil.getQueryWrapper(baseService.getEntityClass())));
        } else {
            if (contentType.contains("x-www-form-urlencoded")) {
                return Result.success(baseService.remove(RequestUtil.getQueryWrapper(baseService.getEntityClass())));
            } else {
                // application/json
                return Result.success(baseService.remove(RequestUtil.getQueryWrapper(baseService.getEntityClass())));
            }
        }
    }
}
