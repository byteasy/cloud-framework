package com.byteasy.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EditPasswordFormDTO {

    @Schema(description = "原始密码")
    private String oldPassword;
    @Schema(description = "新密码")
    private String newPassword;
}
