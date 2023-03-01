package cn.allbs.allbsjwt.entity.cm;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户企业关联表(cm_user_enterprise)表实体类
 *
 * @author chenqi
 * @since 2023-03-01 09:48:56
 */
@Data
@ApiModel(value = "用户企业关联表")
@TableName("cm_user_enterprise")
public class CmUserEnterpriseEntity {

    private static final long serialVersionUID = -13628498558494462L;
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "企业id")
    private Long entId;
}
