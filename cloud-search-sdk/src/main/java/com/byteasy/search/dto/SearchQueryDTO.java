package com.byteasy.search.dto;

import lombok.Data;

@Data
public class SearchQueryDTO {
    /**
     * 原始输入关键词（用于按标题 ILIKE '%xxx%' 走 pg_trgm 子串索引）
     */
    private String rawKeyword;

    /**
     * 经 Java HanLP 分词后的关键词（用于全文检索 websearch_to_tsquery）
     */
    private String segmentedKeyword;

    /**
     * 搜索范围下拉：all(全文) / title(按标题) / person(按人员) / doc(按文档)
     */
    private String searchType = "all";

    /**
     * 顶部业务分类 Tab 过滤
     */
    private Long categoryId;

    /**
     * 子领域/空间过滤
     */
    private Long groupId;

    /**
     * 可选：指定实体类名过滤
     */
    private String className;

    /**
     * 排序方式：relevance(相关度优先) / latest_update(最近更新) / latest_create(最新创建) / earliest_create(最早创建)
     */
    private String sortType = "relevance";

    /**
     * 分页参数
     */
    private Long pageNum = 1L;
    private Long pageSize = 20L;

    public long getOffset() {
        long page = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        long size = (pageSize == null || pageSize < 1) ? 20 : pageSize;
        return (page - 1) * size;
    }
}