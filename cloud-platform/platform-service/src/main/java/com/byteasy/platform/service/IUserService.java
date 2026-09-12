package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Result;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.vo.PersonalSettingsVO;

/**
 * <p>
 * 通用平台用户表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-02-27
 */
public interface IUserService extends IService<User> {

    Result editPassword(String oldPassword, String newPassword);

    Result resetPassword(User user);

    Result updateUserStatus(User user);

    User getUserByUsername(String username);

    User getCurrentLoginUser();

    Result updatePersonalInfo(PersonalSettingsVO personalSettingsVO);

    User getUserByUserId(String userId);
}
