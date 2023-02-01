package cn.allbs.allbsjwt.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限表(sys_menu)表数据库访问层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:56
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

}
