package cn.allbs.allbsjwt.dao.cm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.cm.CmEntDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据测试的表(cm_ent_data)表数据库访问层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:53
 */
@Mapper
public interface CmEntDataDao extends BaseMapper<CmEntDataEntity> {

}
