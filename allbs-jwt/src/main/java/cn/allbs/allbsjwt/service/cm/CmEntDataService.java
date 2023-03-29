package cn.allbs.allbsjwt.service.cm;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.allbs.allbsjwt.entity.cm.CmEntDataEntity;

import java.util.List;

/**
 * 用户数据测试的表(cm_ent_data)表服务接口
 *
 * @author chenqi
 * @since 2023-03-01 09:48:53
 */
public interface CmEntDataService extends IService<CmEntDataEntity> {

    CmEntDataEntity testMapper(Long id);

    List<CmEntDataEntity> customList();
}
