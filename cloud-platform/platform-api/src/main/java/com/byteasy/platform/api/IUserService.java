package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.vo.UserRoleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IUserService",
        path = Constant.SAPI + "/users",
        fallback = IUserServiceFallback.class)
public interface IUserService extends IBaseEntityService<Long, User> {

    @GetMapping("/roles")
    Result<UserRoleDTO> getUserRoleInfo(@RequestParam Map<String, Object> params);
}
