package com.byteasy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.byteasy.annotations.FieldDisplay;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

/**
 * 包含审计字段的基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AuditEntity<ID> extends ImmutablyEntity<ID> {

    /**
     * 更新人ID(用户ID)
     */
    @Schema(description = "更新人编号")
    @TableField(value = "UPDATER_ID", fill = FieldFill.UPDATE, jdbcType = JdbcType.VARCHAR)
    @FieldDisplay(showOrder = 10003, isForm = false, isShow = false)
    private String updaterId;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人")
    @TableField(value = "UPDATED_BY", fill = FieldFill.UPDATE, jdbcType = JdbcType.VARCHAR)  // 标准命名
    @FieldDisplay(showOrder = 10004, isForm = false)
    private String updatedBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "UPDATED_TIME", fill = FieldFill.UPDATE, jdbcType = JdbcType.TIMESTAMP)  // 保持时态一致性
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldDisplay(showOrder = 10005, isForm = false)
    private LocalDateTime updatedTime;

    /**
     * 乐观锁
     */
    @Schema(description = "版本")
    @TableField(value = "VERSION", jdbcType = JdbcType.NUMERIC)
    @Version
    @FieldDisplay(showOrder = 10006, isShow = false, isForm = false)
    private Long version;
}
