package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.Result;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.util.CastUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.entity.UserStatus;
import com.byteasy.platform.mapper.UserMapper;
import com.byteasy.platform.service.IDictionaryService;
import com.byteasy.platform.service.IUserService;
import com.byteasy.platform.util.UserUtil;
import com.byteasy.platform.vo.PersonalSettingsVO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 * 通用平台用户表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-02-27
 */
@Service
@Log4j2
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IDictionaryService dictionaryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                .or().eq(User::getEmail, username)
                .or().eq(User::getMobile, username);
        queryWrapper.ne(User::getStatus, UserStatus.LOGOUT);
        queryWrapper.last("limit 1");
        User user = getOne(queryWrapper, false);
        if (user == null) {
            return null;
        }

        return CastUtil.copyBean(user, PlatformUserDetails.class);
    }

    @Override
    public Result editPassword(String oldPassword, String newPassword) {
        String userName = TokenUtil.getPreferredUserName();
        if (StringUtils.isBlank(userName)) {
            throw new BizException("用户信息缺失!");
        }
        User user = getUserByUsername(userName);
        UserUtil.validate(user);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BizException("原始密码有误!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
        log.info("user: {} editPassword", user.getUsername());
        return Result.success();
    }

    @Override
    public Result resetPassword(User user) {
        User dbUser = getById(user.getId());
        if (!dbUser.getUsername().equals(user.getUsername())) {
            throw new BizException("error.user.edit.forbidden");
        }
        DictionaryItem passwordDefault = dictionaryService.selectByCodeAndItemCode("platform_settings", "default_password");
        dbUser.setPassword(passwordEncoder.encode(passwordDefault.getValue()));
        updateById(dbUser);
        return Result.success();
    }

    @Override
    public Result updateUserStatus(User user) {
        User dbUser = getById(user.getId());
        if (!dbUser.getUsername().equals(user.getUsername())) {
            throw new BizException("error.user.edit.forbidden");
        }
        dbUser.setStatus(user.getStatus());
        updateById(dbUser);
        return Result.success();
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return getOne(queryWrapper, false);
    }

    @Override
    public Result updatePersonalInfo(PersonalSettingsVO personalSettingsVO) {
        User currentLoginUser = getCurrentLoginUser();
        if (!Strings.CS.equals(currentLoginUser.getUsername(), personalSettingsVO.getUsername())) {
            return Result.error(I18nUtil.getMessage("error.user.edit.forbidden"));
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getNickname, personalSettingsVO.getNickname());
        updateWrapper.set(User::getGender, personalSettingsVO.getGender());
        updateWrapper.set(User::getBirthday, personalSettingsVO.getBirthday());
        updateWrapper.set(User::getAvatar, personalSettingsVO.getAvatar());
        updateWrapper.set(User::getMobile, personalSettingsVO.getMobile());
        updateWrapper.set(User::getEmail, personalSettingsVO.getEmail());
        updateWrapper.set(User::getRemark, personalSettingsVO.getRemark());
        updateWrapper.eq(User::getId, currentLoginUser.getId());
        update(updateWrapper);
        return Result.success();
    }

    @Override
    public User getCurrentLoginUser() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, TokenUtil.getPreferredUserName());
        queryWrapper.ne(User::getStatus, UserStatus.LOGOUT);
        queryWrapper.last("limit 1");
        PersonalSettingsVO personalSettingsVO = CastUtil.copyBean(getOne(queryWrapper, false), PersonalSettingsVO.class);
        // TODO
        personalSettingsVO.setOrgName("byteasy");
        return personalSettingsVO;
    }


    @Override
    public User getUserByUserId(String userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, UUID.fromString(userId));
        return getOne(queryWrapper, false);
    }

    @Override
    public boolean saveOrUpdate(User entity) {
        if (entity.getId() == null) {
            DictionaryItem passwordDefault = dictionaryService.selectByCodeAndItemCode("platform_settings", "default_password");
            entity.setPassword(passwordEncoder.encode(passwordDefault.getValue()));
            return super.saveOrUpdate(entity);
        } else {
            User dbUser = getById(entity.getId());
            if (!dbUser.getUsername().equals(entity.getUsername())) {
                throw new BizException("error.user.edit.forbidden");
            }
            dbUser.setRealName(entity.getRealName());
            dbUser.setNickname(entity.getNickname());
            dbUser.setGender(entity.getGender());
            dbUser.setBirthday(entity.getBirthday());
            dbUser.setMobile(entity.getMobile());
            dbUser.setEmail(entity.getEmail());
            dbUser.setStatus(entity.getStatus());
            dbUser.setUserType(entity.getUserType());
            dbUser.setRemark(entity.getRemark());
            return super.saveOrUpdate(dbUser);
        }
    }
}
