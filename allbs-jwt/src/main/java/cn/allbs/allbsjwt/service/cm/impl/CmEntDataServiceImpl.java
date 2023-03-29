package cn.allbs.allbsjwt.service.cm.impl;

import cn.allbs.allbsjwt.dao.cm.CmEntDataDao;
import cn.allbs.allbsjwt.entity.cm.CmEntDataEntity;
import cn.allbs.allbsjwt.service.cm.CmEntDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户数据测试的表(cm_ent_data)表服务实现类
 *
 * @author chenqi
 * @since 2023-03-01 09:48:54
 */
@Service("cmEntDataService")
@AllArgsConstructor
public class CmEntDataServiceImpl extends ServiceImpl<CmEntDataDao, CmEntDataEntity> implements CmEntDataService {

    private final CmEntDataDao cmEntDataDao;

    @Override
    public CmEntDataEntity testMapper(Long id) {
        return this.cmEntDataDao.selectById(id);
    }

    @Override
    public List<CmEntDataEntity> customList() {
        return this.cmEntDataDao.customList();
    }
}
