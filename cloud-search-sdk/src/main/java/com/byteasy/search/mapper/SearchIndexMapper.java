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
     *
     * @param dto 索引入参
     * @return 影响行数
     */
    int upsertIndex(@Param("dto") SearchIndexSaveDTO dto);

    /**
     * 批量 UPSERT（适合首次全量刷数据或批量事件监听写入）
     *
     * @param list 索引入参列表
     * @return 影响行数
     */
    int batchUpsertIndex(@Param("list") List<SearchIndexSaveDTO> list);

    /**
     * 全文综合检索（带高亮与相关性评分）
     *
     * @param query 查询条件
     * @return 匹配结果列表
     */
    List<SearchResultVO> searchFullText(SearchQueryDTO query);

    /**
     * 全文综合检索总命中数（用于分页组件计算总页数）
     *
     * @param query 查询条件
     * @return 命中总条数
     */
    long countFullText(SearchQueryDTO query);

    /**
     * 删除指定实体的索引（实体物理删除时调用）
     *
     * @param className 实体类名
     * @param entityId  业务主键 ID
     * @return 影响行数
     */
    int deleteIndex(@Param("className") String className, @Param("entityId") String entityId);
}
