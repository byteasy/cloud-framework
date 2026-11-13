package com.byteasy.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SearchResultVO {
    private Long categoryId;
    private Long groupId;
    private String className;
    private String entityId;
    private String entityCode;
    private String title;
    private String summary;

    /** 匹配相关度得分 */
    private Float score;

    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime createdTime;
    private String updatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime updatedTime;
}