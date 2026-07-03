package com.byteasy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Authentication;
import com.byteasy.annotations.Log;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.entity.Relation;
import com.byteasy.exception.BizException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


@Log4j2
public abstract class RelationController<SERVICE extends IService<ENTITY>, ENTITY extends Relation> extends AbstractController<SERVICE, ENTITY, Long> implements Constant {

    @GetMapping("")
    @Authentication
    @Operation(summary = "分页查询")
    public Result<Page<? extends ENTITY>> page() throws Exception {
        return Result.success(baseService.page(RequestUtil.getPage(), RequestUtil.getQueryWrapper(baseService.getEntityClass())));
    }

    @PostMapping("/batch")
    @Authentication
    @Operation(summary = "存在更新记录，否则插入记录（批量）")
    @Log
    public Result<List<ENTITY>> saveOrUpdateBatch(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdateBatch: {}", JacksonUtil.toJson(entities));
        }
        if (!baseService.saveOrUpdateBatch(entities)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entities);
    }

    @DeleteMapping("/batch")
    @Authentication
    @Operation(summary = "根据主键批量删除记录")
    @Log
    public Result<Boolean> removeByIds(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeByIds: {}", JacksonUtil.toJson(entities));
        }
        return Result.success(baseService.removeByIds(entities));
    }

    @DeleteMapping("")
    @Authentication
    @Operation(summary = "根据条件批量删除记录（慎重）")
    @Log
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
