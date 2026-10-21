package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(Constant.SAPI + "/files")
public class FileUploadController {

    @Value("${platform.dataRootDir}")
    private String dataRootDir;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @PostMapping("/public")
    @Openapi(summary = "上传公开访问文件", entityName = "com.byteasy.platform.entity.File")
    public Result<String> uploadPublicFile(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        String path = now.getYear() + File.separator + now.getMonthValue() + File.separator + now.format(DateTimeFormatter.ofPattern("ddHHmmss")) +
                "." + FilenameUtils.getExtension(file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PUBLIC, path));

        String accessPath = contextPath + File.separator + path;
        return Result.success(accessPath.replace("\\", "/"));
    }

    @PostMapping("/private")
    @Openapi(summary = "上传授权访问文件", entityName = "com.byteasy.platform.entity.File")
    public Result<String> uploadPrivateFile(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        String path = now.getYear() + File.separator + now.getMonthValue() + File.separator + now.format(DateTimeFormatter.ofPattern("ddHHmmss")) +
                "." + FilenameUtils.getExtension(file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(dataRootDir + File.separator + com.byteasy.platform.Constant.UPLOAD_DIR_PRIVATE, path));

        String accessPath = contextPath + File.separator + path;
        return Result.success(accessPath.replace("\\", "/"));
    }
}
