package com.byteasy.common.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.controller.AbstractController;
import com.byteasy.util.MethodUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.SAPI + "/classMethods")
@Log4j2
public class ClassMethodController {

    @GetMapping
    @Operation(summary = "查询API定义")
    public Result<List<Map<String, String>>> getMethodByKeyWord(@RequestParam(value = "keyword", required = false) String keyword) {
        Map<String, AbstractController> map = SpringUtil.getBeansOfType(AbstractController.class);
        List<Map<String, String>> list = new ArrayList<>();
        for (AbstractController value : map.values()) {
            list.addAll(MethodUtil.listMappingMethod(value));
        }

        if (StringUtils.isNotEmpty(keyword)) {
            return Result.success(list.stream().filter(m -> StringUtils.contains(m.get("httpMethod"), keyword)
                    || StringUtils.contains(m.get("uri"), keyword)
                    || StringUtils.contains(m.get("classMethod"), keyword)
                    || StringUtils.contains(m.get("name"), keyword)
            ).toList());
        } else {
            return Result.success(list);
        }

    }
}
