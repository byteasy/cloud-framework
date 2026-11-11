package com.byteasy.search.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.component.EntityDefineLoadingCache;
import com.byteasy.common.component.FieldDefineLoadingCache;
import com.byteasy.enums.YesNo;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.search.dto.SearchIndexSaveDTO;
import com.byteasy.search.dto.SearchQueryDTO;
import com.byteasy.search.dto.SearchResultVO;
import com.byteasy.search.mapper.SearchIndexMapper;
import com.byteasy.search.util.ChineseSegmenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SearchIndexService {

    private final EntityDefineLoadingCache entityDefineLoadingCache;
    private final FieldDefineLoadingCache fieldDefineLoadingCache;
    private final SearchIndexMapper searchIndexMapper;

    /**
     * 门户全局搜索调用
     */
    public Page<SearchResultVO> search(String rawKeyword, String className, long current, long pageSize) {
        // 对用户输入的搜索词做标准切词
        String segKeyword = ChineseSegmenter.segmentStandard(rawKeyword);

        SearchQueryDTO query = new SearchQueryDTO();
        query.setKeyword(segKeyword);
        query.setClassName(className);
        query.setPageNum(current);
        query.setPageSize(pageSize);

        long total = searchIndexMapper.countFullText(query);
        List<SearchResultVO> list = (total > 0) ? searchIndexMapper.searchFullText(query) : Collections.emptyList();

        return new Page<SearchResultVO>(current, pageSize).setTotal(total).setRecords(list);
    }

    public void handle(EntityChangeEvent event) {
        EntityDefine entityDefine = entityDefineLoadingCache.load(event.getEntityClass());
        if (entityDefine != null && BooleanUtil.isTrue(entityDefine.getFullIndex())) {
            List<FieldDefine> fieldDefines = fieldDefineLoadingCache.load(event.getEntityClass());

            if (fieldDefines == null) {
                log.error("className: {} field define is null", event.getEntityClass());
                return;
            }
            fieldDefines = fieldDefines.stream().filter(s -> YesNo.YES.equals(s.getIsSearch())).toList();
            if (fieldDefines.isEmpty()) {
                return;
            }

            switch (event.getChangeType()) {
                case INSERT, UPDATE -> {
                    for (Object afterEntity : event.getAfterEntities()) {
                        String title = getEntityTitle(afterEntity, fieldDefines);
                        String summary = getEntitySummary(afterEntity, fieldDefines);

                        SearchIndexSaveDTO dto = SearchIndexSaveDTO.builder()
                                .className(event.getEntityClass())
                                .entityId(String.valueOf(BeanUtil.getFieldValue(afterEntity, "id")))
                                .entityCode(String.valueOf(BeanUtil.getFieldValue(afterEntity, "code")))
                                .title(title)
                                .summary(summary)
                                // 使用优化后的 HanLP 切词（带空格）
                                .titleSegmented(ChineseSegmenter.segmentForIndex(title))
                                .summarySegmented(ChineseSegmenter.segmentForIndex(summary))
                                .specsDetailSegmented(null)
                                .remarkSegmented(null)
                                .build();

                        searchIndexMapper.upsertIndex(dto);
                    }
                }
                case DELETE -> {
                    for (Object beforeEntity : event.getBeforeEntities()) {
                        searchIndexMapper.deleteIndex(event.getEntityClass(), String.valueOf(BeanUtil.getFieldValue(beforeEntity, "id")));
                    }
                }
            }
        }
    }

    /**
     * 取第一个可检索字段作为title
     *
     * @param entity
     * @param fieldDefines
     * @return
     */
    private String getEntityTitle(Object entity, List<FieldDefine> fieldDefines) {
        return String.valueOf(BeanUtil.getFieldValue(entity, fieldDefines.getFirst().getFieldName()));
    }

    /**
     * 所有可检索字段作为summary
     *
     * @param entity
     * @param fieldDefines
     * @return
     */
    private String getEntitySummary(Object entity, List<FieldDefine> fieldDefines) {
        StringBuilder sb = new StringBuilder();
        for (FieldDefine fieldDefine : fieldDefines) {
            Object value = BeanUtil.getFieldValue(entity, fieldDefine.getFieldName());
            if (ObjectUtil.isNotEmpty(value)) {
                sb.append(value).append(" ");
            }
        }
        return sb.toString();
    }


}
