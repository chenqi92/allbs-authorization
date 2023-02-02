package cn.allbs.allbsjwt.service.sys.impl;

import cn.allbs.allbsjwt.config.dto.UserInfo;
import cn.allbs.allbsjwt.config.exception.AuthorizationException;
import cn.allbs.allbsjwt.config.vo.MenuVO;
import cn.allbs.allbsjwt.dao.sys.SysUserDao;
import cn.allbs.allbsjwt.entity.sys.SysRoleEntity;
import cn.allbs.allbsjwt.entity.sys.SysUserEntity;
import cn.allbs.allbsjwt.entity.sys.SysUserRoleEntity;
import cn.allbs.allbsjwt.service.sys.SysMenuService;
import cn.allbs.allbsjwt.service.sys.SysRoleService;
import cn.allbs.allbsjwt.service.sys.SysUserRoleService;
import cn.allbs.allbsjwt.service.sys.SysUserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户表(sys_user)表服务实现类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:59
 */
@Service("sysUserService")
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    private final SysUserDao sysUserDao;

    private final SysUserRoleService sysUserRoleService;

    private final SysMenuService sysMenuService;

    private final SysRoleService sysRoleService;

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户
     * @return userInfo
     */
    @Override
    public UserInfo findUserInfoByUserName(String username) {
        SysUserEntity sysUserEntity = sysUserDao.selectOne(Wrappers.<SysUserEntity>query().lambda().eq(SysUserEntity::getUsername, username));
        if (BeanUtil.isEmpty(sysUserEntity)) {
            throw new AuthorizationException("不存在的用户名");
        }
        return this.getUserInfo(sysUserEntity);
    }

    /**
     * 构建userInfo 用户信息 + 角色信息 + 权限集合
     *
     * @param user 用户信息
     * @return 需要构建的用户信息
     */
    private UserInfo getUserInfo(SysUserEntity user) {
        UserInfo userInfo = new UserInfo();
        // 用户基本信息
        userInfo.setSysUser(user);
        // 查询用户对于的角色列表
        Set<Long> roleIds = Optional.ofNullable(sysUserRoleService.list(Wrappers.<SysUserRoleEntity>query()
                        .lambda().eq(SysUserRoleEntity::getUserId, user.getUserId())))
                .orElse(new ArrayList<>()).stream()
                .map(a -> Convert.toLong(a.getRoleId()))
                .collect(Collectors.toSet());
        // 权限标识集合
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId).stream().map(MenuVO::getPermission).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        // 查询并设置角色列表
        List<SysRoleEntity> roleEntities = sysRoleService.list(Wrappers.<SysRoleEntity>query().lambda().in(SysRoleEntity::getRoleId, roleIds));
        if (CollUtil.isNotEmpty(roleEntities)) {
            userInfo.setRoleName(ArrayUtil.toArray(roleEntities.stream().map(SysRoleEntity::getRoleName).collect(Collectors.toList()), String.class));
        }
        return userInfo;
    }
}
