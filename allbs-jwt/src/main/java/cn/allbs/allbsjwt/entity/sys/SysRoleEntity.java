package cn.allbs.allbsjwt.entity.sys;


import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 系统角色表(sys_role)表实体类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Data
@ApiModel(value = "系统角色表")
@TableName("sys_role")
public class SysRoleEntity {

    private static final long serialVersionUID = -61403967321292275L;

    @TableId(value = "role_id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer roleId;

    @ApiModelProperty(value = "")
    private String roleName;

    @ApiModelProperty(value = "")
    private String roleCode;

    @ApiModelProperty(value = "")
    private String roleDesc;

    @ApiModelProperty(value = "")
    private String dsType;

    @ApiModelProperty(value = "")
    private String dsScope;

    @ApiModelProperty(value = "")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    @TableLogic
    private String delFlag;
}
