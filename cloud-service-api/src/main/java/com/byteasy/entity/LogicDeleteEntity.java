package com.byteasy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.enums.Delete;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

/**
 * 逻辑删除基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class LogicDeleteEntity<ID> extends AuditEntity<ID> {

    @Schema(description = "是否删除")
    @TableField(value = "IS_DELETED")
    @TableLogic
    @FieldMeta(showOrder = 10007, isShow = false, isForm = false)
    private Delete isDeleted;

    @Schema(description = "删除人编号")
    @TableField(value = "DELETER_ID", jdbcType = JdbcType.VARCHAR)
    @FieldMeta(showOrder = 10008, isShow = false, isForm = false)
    private String deleterId;

    @Schema(description = "删除人")
    @TableField(value = "DELETED_BY", jdbcType = JdbcType.VARCHAR)
    @FieldMeta(showOrder = 10009, isShow = false, isForm = false)
    private String deletedBy;

    @Schema(description = "删除时间")
    @TableField(value = "DELETED_TIME", jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(showOrder = 10010, isShow = false, isForm = false)
    private LocalDateTime deletedTime;
}
