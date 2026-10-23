package com.byteasy.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.Constant;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.enums.YesNo;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.exception.BizException;
import com.byteasy.mq.IProducer;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.*;
import com.byteasy.platform.mapper.UserRoleMapper;
import com.byteasy.platform.service.*;
import com.byteasy.platform.util.UserValidUtil;
import com.byteasy.platform.vo.LeftMenuVO;
import com.byteasy.platform.vo.ResourceCategoryVO;
import com.byteasy.platform.vo.UserRoleVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 平台角色授权表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    @Autowired
    private IUserService userService;
    @Autowired
    private IResourceGroupService resourceGroupService;
    @Autowired
    private IResourceCategoryService resourceCategoryService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IProducer producer;

    @Override
    public Set<Long> getUserRoleIds(Long userId) {
        return baseMapper.getUserRoleIdsByUserId(userId);
    }

    @Override
    public void removeByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRole(Long userId, Set<Long> roleIds) {
        List<UserRole> list = Lists.newArrayList();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        saveBatch(list);
    }

    @Override
    public UserRoleDTO getUserRoleInfo() {
        QueryWrapper wrapper = RequestUtil.getQueryWrapper(User.class);
        User user = userService.getOne(wrapper, false);
        if (user == null) {
            return null;
        }
        if (!UserStatus.NORMAL.equals(user.getStatus())) {
            throw new BizException("账户被锁定或者禁用");
        }
        UserRoleDTO userRoleVO = new UserRoleDTO();
        userRoleVO.setUser(user);

        userRoleVO.setRoleIds(getUserRoleIds(user.getId()));
        return userRoleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRole(UserRoleVO userRoleVO) {
        if (userRoleVO.getUserId() == null) {
            throw new BizException("缺少用户信息");
        }
        removeByUserId(userRoleVO.getUserId());
        saveUserRole(userRoleVO.getUserId(), userRoleVO.getRoleIds());
        User user = userService.getById(userRoleVO.getUserId());
        producer.publishMessage(Constant.TOPIC_PLATFORM_ENTITY_CHANGE, User.class.getName(), EntityChangeEvent.of(EntityChangeEvent.ChangeType.UPDATE, user, user, User.class.getName()));
    }

    @Override
    public Collection<LeftMenuVO> selectByRouterPathAndLoginUser(String routerPath) {
        User user = userService.getUserByUserId(TokenUtil.getUserId());
        if (user == null) {
            return Collections.emptyList();
        }
        Set<Long> roleIds = getUserRoleIds(user.getId());

        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<ResourceGroup> resourceGroups = resourceGroupService.selectByRoleIds(roleIds);
        if (CollectionUtil.isEmpty(resourceGroups)) {
            return Collections.emptyList();
        }
        /**
         * 自动修改ResourceGroup中的Router，这样即使同一个菜单下权限分配的不一样，也能自动匹配到用户拥有的第一个菜单项
         */
        Set<Long> groupIds = resourceGroups.stream().map(ResourceGroup::getId).collect(Collectors.toSet());
        Set<Long> categoryIds = resourceGroups.stream().map(ResourceGroup::getCategoryId).collect(Collectors.toSet());

        List<Resource> resources = resourceService.selectByRoleIdsAndGroupIds(roleIds, groupIds);
        List<ResourceCategory> categories = resourceCategoryService.list(new LambdaQueryWrapper<ResourceCategory>().in(ResourceCategory::getId, categoryIds));

        Map<Long, List<Resource>> groupResourceMap = resources.stream().collect(Collectors.groupingBy(Resource::getGroupId));
        Map<Long, ResourceCategory> resourceCategoryMap = CastUtil.listToMap(categories, ResourceCategory::getId);

        for (ResourceGroup group : resourceGroups) {
            Optional<Resource> first = groupResourceMap.get(group.getId()).stream().filter(r -> YesNo.YES.equals(r.getVisible())).findFirst();
            ResourceCategory category = resourceCategoryMap.get(group.getCategoryId());
            first.ifPresent(resource -> group.setRouter("/" + StringUtils.joinWith("/", category.getCode(), group.getCode(), resource.getCode())));
        }

        // 取前两级
        String[] arrs = routerPath.split("/");
        if (arrs.length < 3) {
            throw new BizException("路由长度至少3级");
        }
        String rootPath = "/" + arrs[1] + "/" + arrs[2];
        for (ResourceGroup group : resourceGroups) {
            if (group.getRouter().startsWith(rootPath)) {

                List<Resource> list = resourceService.selectByRoleIdsAndGroupIds(roleIds, group.getId());

                List<LeftMenuVO> leftMenuVOS = CastUtil.copyList(list, LeftMenuVO.class);

                Map<Long, LeftMenuVO> map = CastUtil.listToMap(leftMenuVOS, LeftMenuVO::getId);

                List<LeftMenuVO> resultList = new ArrayList<>();
                for (LeftMenuVO leftMenuVO : leftMenuVOS) {
                    if (leftMenuVO.getParentId() == null) {
                        // router url由程序按照路由规则自动生成
                        leftMenuVO.setRouterUrl(StringUtils.joinWith("/", rootPath, leftMenuVO.getCode()));
                        resultList.add(leftMenuVO);
                    } else {
                        LeftMenuVO parent = map.get(leftMenuVO.getParentId());
                        if (parent != null) {
                            if (parent.getChildrenList() == null) {
                                parent.setChildrenList(new ArrayList<>());
                                parent.getChildrenList().add(leftMenuVO);
                            } else {
                                parent.getChildrenList().add(leftMenuVO);
                            }
                        }
                        leftMenuVO.setRouterUrl(leftMenuVO.getCode());
                    }
                }
                updateResourceDirRedirectUri(resultList, map);
                return resultList;
            }
        }
        return List.of();
    }

    /**
     * 更新文件夹路由的重定向属性，因为同一个文件夹内，如果是通过后台配置的重定向地址，可能某些账号没有访问权限，如果是这样，将重定向地址改为动态地址，默认指向文件夹内的第一个菜单或者下一级的文件夹
     *
     * @param leftMenuVOS
     */
    protected void updateResourceDirRedirectUri(Collection<LeftMenuVO> leftMenuVOS, Map<Long, LeftMenuVO> map) {
        if (leftMenuVOS == null) {
            return;
        }
        for (LeftMenuVO leftMenuVO : leftMenuVOS) {
            if (Resource.Type.DIRECTORY.equals(leftMenuVO.getType()) && leftMenuVO.getChildrenList() != null) {
                Optional<LeftMenuVO> first = leftMenuVO.getChildrenList().stream().filter(r -> YesNo.YES.equals(r.getVisible())).findFirst();
                first.ifPresent(leftMenu -> leftMenuVO.setRedirectUrl(getAbsolutePath(leftMenuVO, map) + "/" + leftMenu.getCode()));
            }
            updateResourceDirRedirectUri(leftMenuVO.getChildrenList(), map);
        }
    }

    private String getAbsolutePath(LeftMenuVO leftMenuVO, Map<Long, LeftMenuVO> map) {
        LeftMenuVO parent = map.get(leftMenuVO.getParentId());
        if (parent == null) {
            return leftMenuVO.getRouterUrl();
        } else {
            return getAbsolutePath(parent, map) + "/" + leftMenuVO.getCode();
        }
    }


    /**
     * 顶部导航菜单
     *
     * @return
     */
    @Override
    public List<ResourceCategoryVO> selectCategoryByLoginUser() {
        User user = userService.getUserByUserId(TokenUtil.getUserId());
        UserValidUtil.checkUser(user);
        Set<Long> roleIds = getUserRoleIds(user.getId());

        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        List<ResourceGroup> resourceGroups = resourceGroupService.selectByRoleIds(roleIds);
        if (CollectionUtil.isEmpty(resourceGroups)) {
            return Collections.emptyList();
        }
        /**
         * 自动修改ResourceGroup中的Router，这样即使同一个菜单下权限分配的不一样，也能自动匹配到用户拥有的第一个菜单项
         */
        Set<Long> groupIds = resourceGroups.stream().map(ResourceGroup::getId).collect(Collectors.toSet());
        Set<Long> categoryIds = resourceGroups.stream().map(ResourceGroup::getCategoryId).collect(Collectors.toSet());

        List<ResourceCategory> categories = resourceCategoryService.list(new LambdaQueryWrapper<ResourceCategory>().in(ResourceCategory::getId, categoryIds).orderByAsc(ResourceCategory::getShowOrder));
        List<Resource> resources = resourceService.selectByRoleIdsAndGroupIds(roleIds, groupIds);

        Map<Long, ResourceCategory> resourceCategoryMap = CastUtil.listToMap(categories, ResourceCategory::getId);
        Map<Long, List<Resource>> groupResourceMap = resources.stream().collect(Collectors.groupingBy(Resource::getGroupId));

        /**
         * group菜单改为菜单配置中设置为可见的第一个菜单
         */
        for (ResourceGroup group : resourceGroups) {
            Optional<Resource> first = groupResourceMap.get(group.getId()).stream().filter(r -> YesNo.YES.equals(r.getVisible())).findFirst();
            ResourceCategory category = resourceCategoryMap.get(group.getCategoryId());
            first.ifPresent(resource -> group.setRouter("/" + StringUtils.joinWith("/", category.getCode(), group.getCode(), resource.getCode())));
        }

        Set<Long> parentIds = categories.stream().map(ResourceCategory::getParentId).collect(Collectors.toSet());
        LambdaQueryWrapper<ResourceCategory> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(ResourceCategory::getId, parentIds);
        queryWrapper2.orderByAsc(ResourceCategory::getShowOrder);

        categories.addAll(resourceCategoryService.list(queryWrapper2));

        List<ResourceCategoryVO> resourceCategoryVOList = CastUtil.copyList(categories, ResourceCategoryVO.class);
        List<ResourceCategoryVO> resultList = Lists.newArrayList();

        Map<Long, ResourceCategoryVO> treeMap = CastUtil.listToMap(resourceCategoryVOList, ResourceCategoryVO::getId);
        for (ResourceCategoryVO resourceCategoryVO : resourceCategoryVOList) {
            if (resourceCategoryVO.getParentId() == null) {
                resultList.add(resourceCategoryVO);
            } else {
                ResourceCategoryVO parent = treeMap.get(resourceCategoryVO.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(resourceCategoryVO);
            }
        }

        Map<Long, List<ResourceGroup>> map = resourceGroups.stream().collect(Collectors.groupingBy(ResourceGroup::getCategoryId));

        for (Map.Entry<Long, List<ResourceGroup>> entry : map.entrySet()) {
            ResourceCategoryVO parent = treeMap.get(entry.getKey());
            parent.setResourceGroups(entry.getValue());
        }
        return resultList;
    }
}
