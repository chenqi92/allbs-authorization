package cn.allbs.allbsjwt.entity.sys;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色菜单表(sys_role_menu)表实体类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Data
@ApiModel(value = "角色菜单表")
@TableName("sys_role_menu")
public class SysRoleMenuEntity {

    private static final long serialVersionUID = -54783279637700944L;
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;
}
