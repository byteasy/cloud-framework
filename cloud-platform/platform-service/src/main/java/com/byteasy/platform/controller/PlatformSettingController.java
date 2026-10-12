package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.platform.service.DictionaryUtil;
import com.byteasy.util.I18nUtil;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.byteasy.platform.Constant.*;

@RestController
@RequestMapping(Constant.SAPI + "/platformSettings")
public class PlatformSettingController {

    @Value("${platform.dataRootDir}")
    private String dataRootDir;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final String THEME_PATH = "theme";

    @Autowired
    private DictionaryUtil dictionaryUtil;

    @PostMapping("/favicon")
    @Openapi(summary = "上传favicon文件", entityName = "com.byteasy.platform.entity.ThemeSetting")
    public Result<String> uploadFavicon(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        String path = THEME_PATH + File.separator + "favicon.ico";
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_FAVICON, accessPath);
        return Result.success(accessPath.replace("\\", "/"));
    }

    @PostMapping("/logoImg")
    @Openapi(summary = "上传logo文件", entityName = "com.byteasy.platform.entity.ThemeSetting")
    public Result<String> uploadLogo(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        String path = THEME_PATH + File.separator + "logo." + FilenameUtils.getExtension(img.getOriginalFilename());
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_LOGO_IMG, accessPath);
        return Result.success(accessPath.replace("\\", "/"));
    }

    @PostMapping("/backgroundImg")
    @Openapi(summary = "上传background文件", entityName = "com.byteasy.platform.entity.ThemeSetting")
    public Result<String> uploadBackground(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        String path = THEME_PATH + File.separator + "bg." + FilenameUtils.getExtension(img.getOriginalFilename());
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_BACKGROUND_IMG, accessPath);
        return Result.success(accessPath.replace("\\", "/"));
    }

    @GetMapping("/config")
    @Openapi(summary = "查询平台配置", entityName = "com.byteasy.platform.entity.ThemeSetting")
    public Result getConfig() throws IOException {
        Map<String, String> config = Maps.newHashMap();
        config.put(DIC_SETTING_FAVICON, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_FAVICON, "/platformSvr/assets/favicon.ico"));
        config.put(DIC_SETTING_LOGO_IMG, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_LOGO_IMG, "/platformSvr/assets/logo.png"));
        config.put(DIC_SETTING_BACKGROUND_IMG, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_BACKGROUND_IMG, "/platformSvr/assets/login-background.svg"));
        config.put(DIC_SETTING_TITLE, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_TITLE, I18nUtil.getMessage("login.pageTitle")));
        return Result.success(config);
    }
}
