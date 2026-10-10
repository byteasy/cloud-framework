package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.platform.service.DictionaryUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
    @Openapi(summary = "上传favicon文件")
    public Result<String> uploadFavicon(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        String path = THEME_PATH + File.separator + "favicon.ico";
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_FAVICON, accessPath);
        return Result.success(accessPath.replace("\\", "/"));
    }

    @PostMapping("/logoImg")
    @Openapi(summary = "上传logo文件")
    public Result<String> uploadLogo(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        String path = THEME_PATH + File.separator + "logo." + FilenameUtils.getExtension(img.getOriginalFilename());
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_LOGO_IMG, accessPath);
        return Result.success(accessPath.replace("\\", "/"));
    }

    @PostMapping("/backgroundImg")
    @Openapi(summary = "上传background文件")
    public Result<String> uploadBackground(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        String path = THEME_PATH + File.separator + "bg." + FilenameUtils.getExtension(img.getOriginalFilename());
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        dictionaryUtil.saveOrUpdate(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_BACKGROUND_IMG, accessPath);
        return Result.success(accessPath.replace("\\", "/"));
    }
}
