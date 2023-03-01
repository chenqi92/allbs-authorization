package cn.allbs.allbsjwt.service.cm.impl;

import cn.allbs.allbsjwt.dao.cm.CmUserEnterpriseDao;
import cn.allbs.allbsjwt.entity.cm.CmUserEnterpriseEntity;
import cn.allbs.allbsjwt.service.cm.CmUserEnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户企业关联表(cm_user_enterprise)表服务实现类
 *
 * @author chenqi
 * @since 2023-03-01 09:48:56
 */
@Service("cmUserEnterpriseService")
@AllArgsConstructor
public class CmUserEnterpriseServiceImpl extends ServiceImpl<CmUserEnterpriseDao, CmUserEnterpriseEntity> implements CmUserEnterpriseService {

    private final CmUserEnterpriseDao cmUserEnterpriseDao;
}
