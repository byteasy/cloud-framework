package com.byteasy.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class SearchResultVO {
    private String className;
    private String entityId;
    private String entityCode;
    private String title;
    private String summary;

    /** 匹配相似度得分 */
    private Float score;
    /** 最近更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedTime;
}
