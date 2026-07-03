package com.byteasy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.byteasy.annotations.FieldDisplay;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Entity<ID> implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "编号")
    @TableId(type = IdType.AUTO)
    @FieldDisplay(showOrder = 0, isShow = true, isForm = false)
    private ID id;

    /**
     * 表之外的动态字段
     */
    @TableField(exist = false)
    @FieldDisplay(isShow = false, isForm = false)
    @JsonIgnore
    private final Map<String, Object> extendMap = new HashMap<>();

    @JsonAnySetter
    public void setExtendField(String key, Object value) {
        extendMap.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getExtendField() {
        return extendMap;
    }
}
