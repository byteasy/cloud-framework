package com.byteasy.search.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchIndexSaveDTO {
    // 实体与编码
    private String className;
    private String entityId;
    private String entityCode;

    // 展示字段（原始中文）
    private String title;
    private String summary;

    // --- 以下各字段传入 Java 分词处理后的带空格文本（如 ChineseSegmenter.segmentForIndex(rawText)） ---
    // A 权重字段
    private String titleSegmented;

    // B 权重字段
    private String summarySegmented;

    // C 权重字段
    private String specsDetailSegmented;

    // D 权重字段
    private String remarkSegmented;
}
