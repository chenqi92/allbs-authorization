package cn.allbs.allbsjwt.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类 BaseEntity
 * </p>
 *
 * @author ChenQi
 * @since 2023/3/24 16:57
 */
@Data
public class BaseEntity {

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;
}
