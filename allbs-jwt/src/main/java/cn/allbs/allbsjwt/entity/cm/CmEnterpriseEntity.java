package cn.allbs.allbsjwt.entity.cm;


import cn.allbs.mybatis.datascope.ScopeField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 企业信息表(cm_enterprise)表实体类
 *
 * @author chenqi
 * @since 2023-03-01 09:48:55
 */
@Data
@ApiModel(value = "企业信息表")
@TableName("cm_enterprise")
@ScopeField("id")
public class CmEnterpriseEntity {

    private static final long serialVersionUID = 312203613772143112L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    @TableLogic
    private String delFlag;
}
