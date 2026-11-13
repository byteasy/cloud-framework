package com.byteasy.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchIndexSaveDTO {
    /**
     * 领域分类 ID（必填：对应图片/文档/流程/人员等 Tab）
     */
    private Long categoryId;

    /**
     * 子领域/分组 ID（可选）
     */
    private Long groupId;

    /**
     * 实体类型与主键
     */
    private String className;
    private String entityId;
    private String entityCode;

    /**
     * 原始展示字段
     */
    private String title;
    private String summary;

    /**
     * 创建与更新审计信息
     */
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime createdTime;
    private String updatedBy;

    // --- 以下各字段传入 Java HanLP 分词处理后的带空格文本 ---
    /**
     * A 权重：主标题
     */
    private String titleSegmented;
    /**
     * B 权重：摘要、业务编码
     */
    private String summarySegmented;
    /**
     * C 权重：详情正文、规格参数
     */
    private String specsDetailSegmented;
    /**
     * D 权重：备注信息
     */
    private String remarkSegmented;
}