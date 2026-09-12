package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.ImmutablyEntity;
import com.byteasy.enums.IBaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author devops
 * @since 2026-05-06
 */
@Getter
@Setter
@TableName("platform_login_log")
@Schema(name = "LoginLog", description = "{\"zh_CN\":\"登录日志\",\"en\":\"Login log\"}")
public class LoginLog extends ImmutablyEntity<Long> {

    @Schema(description = "类型")
    @TableField("type")
    @FieldMeta(showOrder = 100, isSearch = true)
    private Type type;

    @Schema(description = "账号")
    @TableField("username")
    @FieldMeta(showOrder = 101)
    private String username;

    @Schema(description = "姓名")
    @TableField("real_name")
    @FieldMeta(showOrder = 102, isSearch = true)
    private String realName;

    @Schema(description = "IP地址")
    @TableField("ip_address")
    @FieldMeta(showOrder = 103)
    private String ipAddress;

    @Schema(description = "Session")
    @TableField("session_id")
    @FieldMeta(showOrder = 104)
    private String sessionId;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 105)
    private String remark;


    public enum Type implements IBaseEnum<Integer> {
        LOGIN(0, "登录"),
        LOGIN_FAILED(1, "登录失败"),
        LOGOUT(2, "登出"),
        ;
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
            throw new IllegalArgumentException("无效的用户状态: " + value);
        }

        @Override
        public String getName() {
            return desc;
        }

        @Override
        public String getCss() {
            if (this.value == 1) {
                return DANGER;
            }
            return IBaseEnum.super.getCss();
        }
    }

}
