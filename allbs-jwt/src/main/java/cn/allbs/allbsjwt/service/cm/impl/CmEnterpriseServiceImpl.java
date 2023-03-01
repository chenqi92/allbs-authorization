package cn.allbs.allbsjwt.service.cm.impl;

import cn.allbs.allbsjwt.dao.cm.CmEnterpriseDao;
import cn.allbs.allbsjwt.entity.cm.CmEnterpriseEntity;
import cn.allbs.allbsjwt.service.cm.CmEnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业信息表(cm_enterprise)表服务实现类
 *
 * @author chenqi
 * @since 2023-03-01 09:48:55
 */
@Service("cmEnterpriseService")
@AllArgsConstructor
public class CmEnterpriseServiceImpl extends ServiceImpl<CmEnterpriseDao, CmEnterpriseEntity> implements CmEnterpriseService {

    private final CmEnterpriseDao cmEnterpriseDao;

    @Override
    public List<CmEnterpriseEntity> findEntListByUserId(Long userId) {
        return cmEnterpriseDao.findEntListByUserId(userId);
    }
}
