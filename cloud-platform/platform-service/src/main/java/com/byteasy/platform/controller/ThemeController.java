package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.platform.configuration.PlatformProperties;
import com.byteasy.platform.service.DictionaryUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import static com.byteasy.platform.Constant.*;

@RestController
@RequestMapping(Constant.PAPI + "/theme")
@Log4j2
public class ThemeController {

    @Autowired
    private DictionaryUtil dictionaryUtil;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private PlatformProperties platformProperties;

    @GetMapping(value = "/title", produces = "text/plain;charset=UTF-8")
    public String getSystemTitle() {
        return dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_TITLE, "Byteasy");
    }

    @GetMapping("/img/{name}")
    public void getThemeFile(HttpServletResponse response, @PathVariable("name") String name) throws IOException {

        String path = dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, name, null);
        if (path.endsWith("ico")) {
            response.setContentType("image/x-icon");
        } else if (path.endsWith("svg")) {
            response.setContentType("image/svg+xml;charset=UTF-8");
        } else if (path.endsWith("jpeg")) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.CACHE_CONTROL, "public, max-age=3600");

        // 2. 判断是否有自定义文件
        if (StringUtils.isNotBlank(path)) {
            path = path.replace(contextPath, "");
            if (path.startsWith(com.byteasy.platform.Constant.PUBLIC_UPLOAD_DIR_PREFIX)) {
                File customFile = new File(platformProperties.getDataRootDir() + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path);

                // 自定义文件存在且是有效文件时输出
                if (customFile.exists() && customFile.isFile()) {
                    try (InputStream in = new FileInputStream(customFile);
                         OutputStream out = response.getOutputStream()) {
                        response.setContentLengthLong(customFile.length());
                        StreamUtils.copy(in, out);
                        out.flush();
                    } catch (Exception e) {
                        log.error("读取自定义 name 文件失败: {}, 降级使用系统默认图标", customFile.getAbsolutePath(), e);
                    }
                } else {
                    log.warn("配置的自定义 name 文件不存在: {}, 自动回退默认图标", customFile.getAbsolutePath());
                }
            } else {
                writeDefaultClassPathFile(response, "static" + path);
            }
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Not found");
        }
    }

    /**
     * 流式输出 Classpath 下的默认 favicon 文件（兼容 JAR 包部署运行）
     */
    private void writeDefaultClassPathFile(HttpServletResponse response, String path) {
        ClassPathResource defaultResource = new ClassPathResource(path);
        if (!defaultResource.exists()) {
            log.error("未找到系统默认 path 资源: {}", path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        try (InputStream in = defaultResource.getInputStream();
             OutputStream out = response.getOutputStream()) {
            response.setContentLengthLong(defaultResource.contentLength());
            StreamUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            log.error("输出默认" + path + "异常", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
