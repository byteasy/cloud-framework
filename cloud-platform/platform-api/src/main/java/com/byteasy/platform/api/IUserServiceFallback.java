package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityServiceFallback;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.User;

import java.util.Map;

public class IUserServiceFallback extends IBaseEntityServiceFallback<Long, User> implements IUserService {


    @Override
    public Result<UserRoleDTO> getUserRoleInfo(Map<String, Object> params) {
        return null;
    }
}
