package com.byteasy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.byteasy.annotations.FieldDisplay;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

public abstract class Relation {
    /**
     * 创建人ID(用户ID)
     */
    @Schema(description = "创建人编号")
    @TableField(value = "CREATOR_ID", fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    @FieldDisplay(showOrder = 10000, isForm = false, isShow = false)
    private String creatorId;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名")
    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)  // 更符合标准命名
    @FieldDisplay(showOrder = 10001, isForm = false)
    private String createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT, jdbcType = JdbcType.TIMESTAMP)  // 保持时态一致性
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldDisplay(showOrder = 10002, isForm = false)
    private LocalDateTime createdTime;
}
