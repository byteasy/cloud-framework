package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import io.swagger.v3.oas.annotations.Operation;
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

    private static final String BASE_PATH = "upload" + File.separator + "files" + File.separator + "public";

    @PostMapping
    @Operation(summary = "上传文件")
    public Result<String> uploadFiles(@RequestPart(value = "file", required = false) MultipartFile img) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String path1 = BASE_PATH + File.separator
                + now.getYear() + File.separator + now.getMonthValue() + File.separator + now.format(DateTimeFormatter.ofPattern("ddHHmmss")) +
                "." + FilenameUtils.getExtension(img.getOriginalFilename());
        FileUtils.copyInputStreamToFile(img.getInputStream(), new File(dataRootDir, path1));

        return Result.success((contextPath + File.separator + path1).replace("\\", "/"));
    }
}
