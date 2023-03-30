package cn.allbs.allbsjwt.dao.cm;

import cn.allbs.allbsjwt.entity.cm.CmEnterpriseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业信息表(cm_enterprise)表数据库访问层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:55
 */
@Mapper
public interface CmEnterpriseDao extends BaseMapper<CmEnterpriseEntity> {

    List<CmEnterpriseEntity> findEntListByUserId(@Param("userId") Long userId);

}
