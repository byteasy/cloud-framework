package com.byteasy.search.mapper;

import com.byteasy.search.dto.SearchIndexSaveDTO;
import com.byteasy.search.dto.SearchQueryDTO;
import com.byteasy.search.dto.SearchResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchIndexMapper {

    /**
     * 单条记录 UPSERT（插入或更新全文索引）
     */
    int upsertIndex(@Param("dto") SearchIndexSaveDTO dto);

    /**
     * 批量 UPSERT（全量刷数或批量事件写入）
     */
    int batchUpsertIndex(@Param("list") List<SearchIndexSaveDTO> list);

    /**
     * 统一多场景检索（自动适配：全文/按标题、分类过滤、4种排序、空搜浏览）
     */
    List<SearchResultVO> searchFullText(SearchQueryDTO query);

    /**
     * 统一检索总命中数（用于分页组件计算总页数）
     */
    long countFullText(SearchQueryDTO query);

    /**
     * 删除指定实体的索引
     */
    int deleteIndex(@Param("className") String className, @Param("entityId") String entityId);
}