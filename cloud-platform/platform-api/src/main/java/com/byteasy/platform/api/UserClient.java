package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.dto.RoleUserDTO;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange(Constant.SAPI + "/users")
public interface UserClient extends IBaseEntityService<Long, User> {

    @GetExchange("/roles")
    Result<UserRoleDTO> getUserRoleInfo(@RequestParam Map<String, Object> params);

    @GetExchange("/roles/{roleId}/users")
    Result<RoleUserDTO> getUsersByRoleId(@PathVariable Long roleId);
}
