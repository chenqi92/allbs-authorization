package cn.allbs.allbsjwt.config.dto;

import cn.allbs.allbsjwt.entity.sys.SysUserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 类 UserInfo
 * </p>
 * 用户信息
 *
 * @author ChenQi
 * @since 2023/2/1 17:30
 */
@Data
@Schema(title = "用户信息", name = "UserInfo")
public class UserInfo implements Serializable {

    /**
     * 用户基本信息
     */
    @Schema(description = "用户基本信息", name = "sysUser", implementation = SysUserEntity.class)
    private SysUserEntity sysUser;
    /**
     * 权限标识集合
     */
    @Schema(description = "权限标识集合", name = "permissions")
    private String[] permissions;

    /**
     * 角色名称标识名称集合
     */
    @Schema(description = "角色名称标识集合", name = "roleName")
    private String[] roleName;

    /**
     * 用户关联的企业列表
     */
    @Schema(description = "用户关联的企业id", name = "entIds")
    private Set<Long> entIds;
}
