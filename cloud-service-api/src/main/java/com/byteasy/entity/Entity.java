package com.byteasy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.byteasy.annotations.FieldMeta;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

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
    @FieldMeta(showOrder = 0, isShow = true, isForm = false)
    private ID id;


    /**
     * 表之外的动态字段
     */
    @TableField(exist = false)
    @FieldMeta(isShow = false, isForm = false)
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
