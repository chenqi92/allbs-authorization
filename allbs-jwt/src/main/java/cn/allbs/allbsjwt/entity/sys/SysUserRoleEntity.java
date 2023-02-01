package cn.allbs.allbsjwt.entity.sys;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户角色表(sys_user_role)表实体类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:59
 */
@Data
@ApiModel(value = "用户角色表")
@TableName("sys_user_role")
public class SysUserRoleEntity {

    private static final long serialVersionUID = 376244131139875695L;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;
}
