package com.byteasy.platform;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.byteasy.common.controller.AuditController;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public class CodeOracleGenerate {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:postgresql://192.168.56.102:5432/byteasy?currentSchema=byteasy_cloud_platform",
                        "postgres", "Xsy870712@@@")
                .globalConfig(builder -> builder
                        .author("devops")
                        .outputDir(SystemUtils.getUserDir().getAbsolutePath() + File.separator + "cloud-platform" + File.separator + "platform-service" + File.separator + "src" + File.separator + "main" + File.separator + "java")
                        .commentDate("yyyy-MM-dd")
                        .enableSwagger()
                        .enableSpringdoc()
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent("com.byteasy.platform")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addInclude("platform_resource_layout")
                        .addInclude("platform_std_layout_params")
                        .addTablePrefix("platform_")
                        .entityBuilder()
                        .addSuperEntityColumns("id", "version", "deleted_time", "deleted_by", "deleter_id", "is_deleted", "updated_time", "updated_by", "updater_id", "created_time", "created_by", "creator_id")
                        .enableTableFieldAnnotation()
                        .enableLombok()
                        .controllerBuilder().enableRestStyle().superClass(AuditController.class)
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
