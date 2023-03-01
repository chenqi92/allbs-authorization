package cn.allbs.allbsjwt.dao.cm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.cm.CmUserEnterpriseEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户企业关联表(cm_user_enterprise)表数据库访问层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:56
 */
@Mapper
public interface CmUserEnterpriseDao extends BaseMapper<CmUserEnterpriseEntity> {

}
