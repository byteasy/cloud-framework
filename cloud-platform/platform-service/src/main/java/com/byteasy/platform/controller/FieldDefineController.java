package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.Status;
import com.byteasy.annotations.Authentication;
import com.byteasy.common.controller.AuditController;
import com.byteasy.dto.Column;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.platform.service.IFieldDefineService;
import com.byteasy.platform.service.INacosConfigService;
import com.byteasy.platform.vo.SchemaClassTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 列配置表（存储业务实体字段的展示/校验/表单等配置） 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-13
 */
@RestController
@RequestMapping(Constant.SAPI + "/fieldDefines")
public class FieldDefineController extends AuditController<Long, IFieldDefineService, FieldDefine> {
    @Autowired
    private INacosConfigService nacosConfigService;

    @GetMapping("/entity/{className}")
    @Operation(summary = "查询业务对象的字段信息")
    public List<Column> getSchemaMetaInfo(@PathVariable("className") String className) throws Exception {
        return baseService.queryByClassName(className);
    }

    @PutMapping("/{serviceName}/{className}")
    @Operation(summary = "同步代码中的业务对象属性定义")
    @Authentication
    public Result syncSchemaMetaInfo(@PathVariable("serviceName") String serviceName, @PathVariable("className") String className, @RequestBody List<FieldDefine> fieldDefines) throws Exception {
        baseService.syncSchemaMetaInfo(serviceName, className, fieldDefines);
        return Result.success();
    }


    @GetMapping("/services")
    @Operation(summary = "查询所有的注册服务")
    public Result<List<String>> listServices() throws Exception {
        return Result.success(nacosConfigService.getAllServices());
    }

    @Deprecated
    @GetMapping("/services/classes")
    @Operation(summary = "查询服务下的所有注册模型")
    public Result<List<SchemaClassTreeVO>> listServicesClassTree() throws Exception {
        return Result.success();
    }

    @GetMapping("/services/{serviceName}/context")
    @Operation(summary = "查询服务的contextPath")
    public Result<String> getContextPath(@PathVariable("serviceName") String serviceName) throws Exception {
        return Result.of(Status.SUCCESS, nacosConfigService.getContextPath(serviceName));
    }

}
