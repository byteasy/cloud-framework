package com.byteasy.common.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfo {

    @JsonProperty("family_name")
    @Schema(description = "姓/姓氏")
    private String familyName;

    @JsonProperty("given_name")
    @Schema(description = "名/教名/名字")
    private String givenName;

    @Schema(description = "完整姓名")
    private String name;

    @JsonProperty("sub")
    @Schema(description = "用户ID")
    private String userId;

    @JsonProperty("preferred_username")
    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "用户邮箱")
    private String email;

    @JsonProperty("email_verified")
    @Schema(description = "邮箱是否验证")
    private Boolean emailVerified;

    @JsonProperty("mobile")
    @Schema(description = "手机号码")
    private String mobile;

    @JsonProperty("mobile_verified")
    @Schema(description = "电话是否验证")
    private Boolean mobileVerified;

    @Schema(description = "用户配置的显示语言")
    private String locale;

    @Schema(description = "用户领域")
    private String realm;

    @Schema(description = "true: 用户发起的请求 false: 微服务通过上下文传递")
    private boolean forwardBy = false;

}
