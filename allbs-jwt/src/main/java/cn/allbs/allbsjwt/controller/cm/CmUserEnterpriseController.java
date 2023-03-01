package cn.allbs.allbsjwt.controller.cm;

import cn.allbs.allbsjwt.entity.cm.CmUserEnterpriseEntity;
import cn.allbs.allbsjwt.service.cm.CmUserEnterpriseService;
import cn.allbs.common.utils.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户企业关联表(cm_user_enterprise)表控制层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:56
 */
@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/cm/user/enterprise")
@Api(value = "cmUserEnterprise", tags = "用户企业关联表 管理")
public class CmUserEnterpriseController {

    /**
     * 服务对象
     */
    private final CmUserEnterpriseService cmUserEnterpriseService;

    /**
     * 分页查询
     *
     * @param page                   分页对象
     * @param cmUserEnterpriseEntity 用户企业关联表
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('cmUserEnterprise_list')")
    public R getCmUserEnterprisePage(Page page, CmUserEnterpriseEntity cmUserEnterpriseEntity) {
        return R.ok(this.cmUserEnterpriseService.page(page, Wrappers.query(cmUserEnterpriseEntity)));
    }


    /**
     * 新增 用户企业关联表
     *
     * @param cmUserEnterpriseEntity 用户企业关联表
     * @return R
     */
    @ApiOperation(value = "新增用户企业关联表", notes = "新增用户企业关联表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('cmUserEnterprise_save')")
    public R save(@RequestBody CmUserEnterpriseEntity cmUserEnterpriseEntity) {
        return R.ok(this.cmUserEnterpriseService.save(cmUserEnterpriseEntity));
    }

    /**
     * 修改用户企业关联表
     *
     * @param cmUserEnterpriseEntity 用户企业关联表
     * @return R
     */
    @ApiOperation(value = "修改用户企业关联表", notes = "修改用户企业关联表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('cmUserEnterprise_update')")
    public R updateById(@RequestBody CmUserEnterpriseEntity cmUserEnterpriseEntity) {
        return R.ok(this.cmUserEnterpriseService.updateById(cmUserEnterpriseEntity));
    }
}
