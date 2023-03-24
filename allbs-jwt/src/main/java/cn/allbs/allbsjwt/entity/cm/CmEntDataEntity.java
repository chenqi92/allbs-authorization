package cn.allbs.allbsjwt.entity.cm;


import cn.allbs.allbsjwt.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户数据测试的表(cm_ent_data)表实体类
 *
 * @author chenqi
 * @since 2023-03-01 09:48:53
 */
@Data
@ApiModel(value = "用户数据测试的表")
@TableName("cm_ent_data")
public class CmEntDataEntity extends BaseEntity {

    private static final long serialVersionUID = 891006607770794112L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "企业id")
    private Long entId;

    @ApiModelProperty(value = "说明")
    private String description;
}
