package cn.allbs.allbsjwt.service.cm;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.allbs.allbsjwt.entity.cm.CmEnterpriseEntity;

import java.util.List;

/**
 * 企业信息表(cm_enterprise)表服务接口
 *
 * @author chenqi
 * @since 2023-03-01 09:48:55
 */
public interface CmEnterpriseService extends IService<CmEnterpriseEntity> {

    List<CmEnterpriseEntity> findEntListByUserId(Long userId);
}
