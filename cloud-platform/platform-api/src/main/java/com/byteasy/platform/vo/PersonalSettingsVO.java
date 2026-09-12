package com.byteasy.platform.vo;

import com.byteasy.platform.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "PersonalSettingsVO", description = "个人基本资料")
public class PersonalSettingsVO extends User {

    @Schema(description = "所属部门")
    private String orgName;
}
