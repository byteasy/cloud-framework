package com.byteasy.platform.controller;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Constant;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constant.SAPI + "/scriptFunctions")
public class ScriptFunctionController {

    @Autowired
    private ICommonService commonService;

    @RequestMapping("/register")
    public List<Map<String, Object>> register(@RequestBody List<Map<String, Object>> scriptFunctions) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", scriptFunctions.stream().map(v -> MapUtil.getStr(v, "name")).collect(Collectors.toSet()));
        map.put("nameQueryType", "in");
        List<Map<String, Object>> dbFunctions = commonService.list("com.byteasy.platform.entity.ScriptFunction", map);

        Map<String, Map<String, Object>> dbMap = CastUtil.listToMap(dbFunctions, v -> MapUtil.getStr((Map)v, "name"));

        List<Map<String, Object>> dbNotExists = scriptFunctions.stream().filter(v -> !dbMap.containsKey(v.get("name"))).toList();
        commonService.saveOrUpdateBatch("com.byteasy.platform.entity.ScriptFunction", dbNotExists);

        List<Map<String, Object>> needUpdates = scriptFunctions.stream().filter(v -> !dbMap.get(v.get("name")).get("description").equals(v.get("description"))).map(
                v -> {
                    Map<String, Object> dbRow = dbMap.get(v.get("name"));
                    dbRow.put("description", v.get("description"));
                    return dbRow;
                }
        ).toList();
        commonService.saveOrUpdateBatch("com.byteasy.platform.entity.ScriptFunction", needUpdates);

        return commonService.list("com.byteasy.platform.entity.ScriptFunction", Collections.EMPTY_MAP);
    }
}
