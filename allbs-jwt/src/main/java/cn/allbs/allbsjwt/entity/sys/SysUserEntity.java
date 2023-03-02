package cn.allbs.allbsjwt.entity.sys;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表(sys_user)表实体类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:58
 */
@Data
@ApiModel(value = "用户表")
@TableName("sys_user")
public class SysUserEntity {

    private static final long serialVersionUID = 268029960688673404L;

    @TableId(value = "user_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long userId;

    @ApiModelProperty(value = "")
    private String username;

    @ApiModelProperty(value = "")
    private String password;

    @ApiModelProperty(value = "")
    private transient String salt;

    @ApiModelProperty(value = "")
    private String phone;

    @ApiModelProperty(value = "")
    private String avatar;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    private String lockFlag;

    @ApiModelProperty(value = "")
    @TableLogic
    private String delFlag;
}
