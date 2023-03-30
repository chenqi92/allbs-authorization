package cn.allbs.allbsjwt.dao.cm;

import cn.allbs.allbsjwt.entity.cm.CmEntDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户数据测试的表(cm_ent_data)表数据库访问层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:53
 */
@Mapper
public interface CmEntDataDao extends BaseMapper<CmEntDataEntity> {

    List<CmEntDataEntity> customList();

}
