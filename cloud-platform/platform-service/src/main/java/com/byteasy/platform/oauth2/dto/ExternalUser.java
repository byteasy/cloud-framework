package com.byteasy.platform.oauth2.dto;

import com.byteasy.common.token.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalUser extends UserInfo {

    @Schema(description = "跳转路由地址，无http(s}等，仅前端的路由地址")
    private String path;
}
