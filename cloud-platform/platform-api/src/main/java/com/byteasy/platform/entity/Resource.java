package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.IBaseEnum;
import com.byteasy.enums.YesNo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台资源定义表（菜单/目录/按钮权限）
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Getter
@Setter
@TableName("platform_resource")
@Schema(name = "Resource", description = "{\"zh_CN\":\"系统菜单\",\"en\":\"System Menu\"}")
public class Resource extends AuditEntity<Long> {

    @Schema(description = "编码")
    @TableField("code")
    @FieldMeta(showOrder = 101, isSearch = true, isRequire = true)
    private String code;

    @Schema(description = "资源分组")
    @TableField("group_id")
    @FieldMeta(showOrder = 102, isShow = false)
    private Long groupId;

    @Schema(description = "上级资源")
    @TableField("parent_id")
    @FieldMeta(showOrder = 103, isShow = false)
    private Long parentId;

    @Schema(description = "名称")
    @TableField("name")
    @FieldMeta(showOrder = 104, isI18n = true, isSearch = true, isRequire = true)
    private String name;

    @Schema(description = "图标")
    @TableField("icon")
    @FieldMeta(showOrder = 105, maxLength = 2048)
    private String icon;

    @Schema(description = "是否显示")
    @TableField("visible")
    @FieldMeta(showOrder = 106)
    private YesNo visible;

    @Schema(description = "资源类型")
    @TableField("type")
    @FieldMeta(showOrder = 107, isRequire = true)
    private Type type;

    @Schema(description = "是否打开tab")
    @TableField("open_tab")
    @FieldMeta(showOrder = 108)
    private YesNo openTab;

    @Schema(description = "是否固定")
    @TableField("affix")
    @FieldMeta(showOrder = 109)
    private YesNo affix;

    @Schema(description = "是否缓存")
    @TableField("keep_alive")
    @FieldMeta(showOrder = 110)
    private YesNo keepAlive;

    @Schema(description = "显示顺序")
    @TableField("show_order")
    @FieldMeta(showOrder = 111)
    private Integer showOrder;

    @Schema(description = "布局")
    @TableField("layout")
    @FieldMeta(showOrder = 112)
    private String layout;

    @Schema(description = "前端组件")
    @TableField("component")
    @FieldMeta(showOrder = 113, maxLength = 200)
    private String component;

    /**
     * 仅文件夹有此属性
     * 无需后台人为配置，改为代码自动维护，根据用户所拥有的权限，默认指向到childrenList的第一项
     */
    @Schema(description = "重定向地址")
    @TableField("redirect_url")
    @FieldMeta(showOrder = 115, isShow = false, isForm = false)
    private String redirectUrl;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 116, isI18n = true)
    private String remark;

    public enum Type implements IBaseEnum<Integer> {
        DIRECTORY(0, "目录"), MENU(1, "菜单");

        public final int value;
        public final String desc;

        Type(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static Type fromValue(int value) {
            for (Type status : Type.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("无效的状态值: " + value);
        }

        @Override
        public String getName() {
            return desc;
        }
    }
}
