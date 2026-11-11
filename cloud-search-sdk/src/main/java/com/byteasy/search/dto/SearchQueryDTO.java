package com.byteasy.search.dto;

import lombok.Data;

@Data
public class SearchQueryDTO {
    /**
     * 搜索关键词（传入经 Java 分词后的文本，如 "华为 手机"）
     */
    private String keyword;

    /**
     * 可选：按实体类型过滤（如 Product, Article）
     */
    private String className;

    /**
     * 分页参数
     */
    private Long pageNum = 1L;
    private Long pageSize = 20L;

    /**
     * 计算分页 offset
     */
    public Long getOffset() {
        return (Math.max(pageNum, 1) - 1) * pageSize;
    }
}
