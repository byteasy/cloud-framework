package com.byteasy.search.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.component.EntityDefineLoadingCache;
import com.byteasy.common.component.FieldDefineLoadingCache;
import com.byteasy.enums.YesNo;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.platform.api.ResourceGroupClient;
import com.byteasy.platform.dto.ResourceGroupDTO;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SearchIndexService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.ofHours(8);

    private final EntityDefineLoadingCache entityDefineLoadingCache;
    private final FieldDefineLoadingCache fieldDefineLoadingCache;
    private final SearchIndexMapper searchIndexMapper;
    private final ResourceGroupClient resourceGroupClient;

    /**
     * 门户全局搜索调用
     */
    public Page<SearchResultVO> search(SearchQueryDTO searchQueryDTO) {
        // 对用户输入的搜索词做标准切词
        String segKeyword = ChineseSegmenter.segmentStandard(searchQueryDTO.getRawKeyword());

        searchQueryDTO.setSegmentedKeyword(segKeyword);

        long total = searchIndexMapper.countFullText(searchQueryDTO);
        List<SearchResultVO> list = (total > 0) ? searchIndexMapper.searchFullText(searchQueryDTO) : Collections.emptyList();

        return new Page<SearchResultVO>(searchQueryDTO.getPageNum(), searchQueryDTO.getPageSize()).setTotal(total).setRecords(list);
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

            // 加载领域分类相关信息
            ResourceGroupDTO resourceGroupDTO = resourceGroupClient.findGroupByClassName(event.getEntityClass()).getIfPresent();

            switch (event.getChangeType()) {
                case INSERT, UPDATE -> {
                    for (Object afterEntity : event.getAfterEntities()) {
                        String title = getEntityTitle(afterEntity, fieldDefines);
                        String summary = getEntitySummary(afterEntity, fieldDefines);

                        SearchIndexSaveDTO dto = SearchIndexSaveDTO.builder()
                                .categoryId(resourceGroupDTO.getCategoryId())
                                .groupId(resourceGroupDTO.getId())
                                .className(event.getEntityClass())
                                .entityId(String.valueOf(BeanUtil.getFieldValue(afterEntity, "id")))
                                .entityCode(getEntityCode(afterEntity))
                                .createdBy(String.valueOf(BeanUtil.getFieldValue(afterEntity, "createdBy")))
                                .createdTime(getOffsetDateTime(afterEntity, "createdTime"))
                                .updatedBy(String.valueOf(BeanUtil.getFieldValue(afterEntity, "updatedBy")))
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
     * 兼容多种时间类型的安全转换，避免 LocalDateTime 直接 OffsetDateTime.from 报错
     */
    private OffsetDateTime getOffsetDateTime(Object entity, String field) {
        Object value = BeanUtil.getFieldValue(entity, field);
        if (value == null) {
            return null;
        }
        if (value instanceof OffsetDateTime odt) {
            return odt;
        }
        if (value instanceof LocalDateTime ldt) {
            return ldt.atOffset(DEFAULT_ZONE_OFFSET);
        }
        if (value instanceof Instant instant) {
            return instant.atOffset(DEFAULT_ZONE_OFFSET);
        }
        if (value instanceof Date date) {
            return date.toInstant().atOffset(DEFAULT_ZONE_OFFSET);
        }
        if (value instanceof String str && !str.isBlank()) {
            try {
                return OffsetDateTime.of(LocalDateTime.parse(str.trim(), DATE_TIME_FORMATTER), DEFAULT_ZONE_OFFSET);
            } catch (Exception e) {
                return OffsetDateTime.parse(str.trim());
            }
        }
        return null;
    }

    private String getEntityCode(Object entity) {
        Object val = BeanUtil.getFieldValue(entity, "code");
        if (ObjectUtil.isEmpty(val)) {
            val = BeanUtil.getFieldValue(entity, "id");
        }
        return Convert.toStr(val, "");
    }

    /**
     * 取第一个可检索字段作为 title
     */
    private String getEntityTitle(Object entity, List<FieldDefine> fieldDefines) {
        Object val = BeanUtil.getFieldValue(entity, fieldDefines.getFirst().getFieldName());
        return Convert.toStr(val, "");
    }

    /**
     * 所有可检索字段拼接作为 summary
     */
    private String getEntitySummary(Object entity, List<FieldDefine> fieldDefines) {
        StringBuilder sb = new StringBuilder();
        for (FieldDefine fieldDefine : fieldDefines) {
            Object value = BeanUtil.getFieldValue(entity, fieldDefine.getFieldName());
            if (ObjectUtil.isNotEmpty(value)) {
                sb.append(value).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
