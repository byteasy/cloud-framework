package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.LogicDeleteEntity;
import com.byteasy.enums.Gender;
import com.byteasy.enums.YesNo;
import com.byteasy.typehandler.PgJsonbTypeHandler;
import com.byteasy.typehandler.StringToUuidTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 通用平台用户表
 * </p>
 *
 * @author devops
 * @since 2026-02-27
 */
@Getter
@Setter
@TableName("platform_user")
@Schema(name = "User", description = "{\"zh_CN\":\"系统账号\",\"en\":\"System Account\"}")
public class User extends LogicDeleteEntity<Long> {

    @Schema(description = "头像")
    @TableField("avatar")
    @FieldMeta(showOrder = 101, isForm = false)
    private String avatar;

    @Schema(description = "用户编号")
    @TableField(value = "user_id", typeHandler = StringToUuidTypeHandler.class)
    @FieldMeta(isForm = false, isOverflow = true, showOrder = 102)
    private String userId;

    @Schema(description = "用户名")
    @TableField("username")
    @FieldMeta(showOrder = 1, isSearch = true, isRequire = true, minLength = 4, maxLength = 20)
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    @FieldMeta(isShow = false, isForm = false, maxLength = 200)
    private String password;

    @Schema(description = "密码盐")
    @TableField("salt")
    @FieldMeta(isShow = false, isForm = false)
    private String salt;

    @Schema(description = "账号过期时间")
    @TableField("account_expired_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(isShow = false, isForm = false)
    private LocalDateTime accountExpiredAt;

    @Schema(description = "密码过期时间")
    @TableField("credentials_expired_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(isShow = false, isForm = false)
    private LocalDateTime credentialsExpiredAt;

    @Schema(description = "姓名")
    @TableField("real_name")
    @FieldMeta(showOrder = 103, isSearch = true, isRequire = true)
    private String realName;

    @Schema(description = "昵称")
    @TableField("nickname")
    @FieldMeta(showOrder = 104, isSearch = true)
    private String nickname;

    @Schema(description = "性别")
    @TableField("gender")
    @FieldMeta(showOrder = 105)
    private Gender gender;

    @Schema(description = "出生日期")
    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @FieldMeta(showOrder = 106)
    private LocalDate birthday;

    @Schema(description = "手机")
    @TableField("mobile")
    @FieldMeta(showOrder = 107, isSearch = true)
    private String mobile;

    @Schema(description = "手机是否验证")
    @TableField("mobile_verified")
    @FieldMeta(isShow = false, isForm = false)
    private YesNo mobileVerified;

    @Schema(description = "邮箱")
    @TableField("email")
    @FieldMeta(showOrder = 108, isSearch = true)
    private String email;

    @Schema(description = "邮箱是否验证")
    @TableField("email_verified")
    @FieldMeta(isShow = false, isForm = false)
    private YesNo emailVerified;

    @Schema(description = "状态")
    @TableField("status")
    @FieldMeta(showOrder = 109, isRequire = true, isSearch = true)
    private UserStatus status;

    @Schema(description = "类型")
    @TableField("user_type")
    @FieldMeta(showOrder = 110, isRequire = true, isSearch = true)
    private UserType userType;

    @Schema(description = "登录失败次数")
    @TableField("login_fail_count")
    @FieldMeta(isShow = false, isForm = false)
    private Integer loginFailCount;

    @Schema(description = "账号锁定时间")
    @TableField("lock_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(isShow = false, isForm = false)
    private LocalDateTime lockTime;

    @Schema(description = "最后登录时间")
    @TableField("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(showOrder = 111, isForm = false)
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    @TableField("last_login_ip")
    @FieldMeta(showOrder = 112, isForm = false)
    private String lastLoginIp;

    @Schema(description = "租户编号")
    @TableField("tenant_id")
    @FieldMeta(isShow = false, isForm = false)
    private String tenantId;

    @Schema(description = "领域编号")
    @TableField("realm_id")
    @FieldMeta(isShow = false, isForm = false)
    private Long realmId;

    @Schema(description = "组织编号")
    @TableField("org_id")
    @FieldMeta(isShow = false, isForm = false)
    private Long orgId;

    @Schema(description = "部门编号")
    @TableField("dept_id")
    @FieldMeta(isShow = false, isForm = false)
    private Long deptId;

    @Schema(description = "扩展属性")
    @TableField(value = "ext_info", typeHandler = PgJsonbTypeHandler.class)
    @FieldMeta(isShow = false, isForm = false)
    private Map<String, String> extInfo;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 113, isShow = false, isI18n = true)
    private String remark;
}
