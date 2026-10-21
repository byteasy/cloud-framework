package com.byteasy.platform.controller;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.platform.service.DictionaryUtil;
import com.byteasy.util.I18nUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.byteasy.platform.Constant.*;

@RestController
@RequestMapping(Constant.SAPI + "/platformSettings")
public class PlatformSettingController {

    @Autowired
    private DictionaryUtil dictionaryUtil;

    @PostMapping("/config")
    @Openapi(summary = "保存平台配置", entityName = "com.byteasy.platform.entity.ThemeSetting")
    public Result saveConfig(@RequestBody Map<String, String> map) {
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_FAVICON, MapUtil.getStr(map, DIC_SETTING_FAVICON, "/platformSvr/assets/favicon.ico"));
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_LOGO_IMG, MapUtil.getStr(map, DIC_SETTING_LOGO_IMG, "/platformSvr/assets/logo.png"));
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_BACKGROUND_IMG, MapUtil.getStr(map, DIC_SETTING_BACKGROUND_IMG, "/platformSvr/assets/login-background.svg"));
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_TITLE, MapUtil.getStr(map, DIC_SETTING_TITLE, I18nUtil.getMessage("login.pageTitle")));
        return Result.success(map);
    }

    @GetMapping("/config")
    @Openapi(summary = "查询平台配置", entityName = "com.byteasy.platform.entity.ThemeSetting")
    public Result getConfig() {
        Map<String, String> config = Maps.newHashMap();
        config.put(DIC_SETTING_FAVICON, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_FAVICON, "/platformSvr/assets/favicon.ico"));
        config.put(DIC_SETTING_LOGO_IMG, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_LOGO_IMG, "/platformSvr/assets/logo.png"));
        config.put(DIC_SETTING_BACKGROUND_IMG, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_BACKGROUND_IMG, "/platformSvr/assets/login-background.svg"));
        config.put(DIC_SETTING_TITLE, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_TITLE, I18nUtil.getMessage("login.pageTitle")));
        return Result.success(config);
    }
}
