package cn.allbs.allbsjwt.entity.sys;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单权限表(sys_menu)表实体类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:56
 */
@Data
@ApiModel(value = "菜单权限表")
@TableName("sys_menu")
public class SysMenuEntity {

    private static final long serialVersionUID = 289645823334618395L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String permission;

    @ApiModelProperty(value = "")
    private String path;

    @ApiModelProperty(value = "父菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "")
    private String icon;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "")
    private String keepAlive;

    @ApiModelProperty(value = "")
    private String type;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    @TableLogic
    private String delFlag;
}
