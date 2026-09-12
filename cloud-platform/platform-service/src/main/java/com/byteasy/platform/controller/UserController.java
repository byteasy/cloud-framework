package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Authentication;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.dto.EditPasswordFormDTO;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.service.IUserRoleService;
import com.byteasy.platform.service.IUserService;
import com.byteasy.platform.vo.PersonalSettingsVO;
import com.byteasy.platform.vo.UserRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.SAPI + "/users")
public class UserController extends AuditController<Long, IUserService, User> {
    @Autowired
    private IUserRoleService userRoleService;

    @PutMapping("/password")
    @Authentication
    @Operation(summary = "修改密码")
    public Result updatePassword(@RequestBody EditPasswordFormDTO editPasswordFormDTO) {
        return baseService.editPassword(editPasswordFormDTO.getOldPassword(), editPasswordFormDTO.getNewPassword());
    }

    @PutMapping("/status")
    @Authentication
    @Operation(summary = "修改密码")
    public Result updateUserStatus(@RequestBody User user) {
        return baseService.updateUserStatus(user);
    }

    @DeleteMapping("/password")
    @Authentication
    @Operation(summary = "重置密码")
    public Result resetPassword(@RequestBody User user) {
        return baseService.resetPassword(user);
    }

    @PutMapping("/personalInfo")
    @Authentication
    @Operation(summary = "修改个人信息")
    public Result updatePersonalInfo(@RequestBody PersonalSettingsVO personalSettingsVO) {
        return baseService.updatePersonalInfo(personalSettingsVO);
    }

    @GetMapping("/personalInfo")
    @Authentication
    @Operation(summary = "查询个人信息")
    public Result<User> getPersonalInfo() {
        return Result.success(baseService.getCurrentLoginUser());
    }

    @GetMapping("/roles")
    @Authentication
    @Operation(summary = "查询账户角色信息")
    public Result<UserRoleVO> getUserRoleInfo(@RequestParam("userId") Long userId) {
        return Result.success(userRoleService.getUserRoleInfo(userId));
    }

    @PostMapping("/roles")
    @Authentication
    @Operation(summary = "保存账户角色信息")
    public Result<UserRoleVO> saveUserRole(@RequestBody UserRoleVO userRoleVO) {
        userRoleService.saveUserRole(userRoleVO);
        return Result.success();
    }
}
