package cn.allbs.allbsjwt.controller.cm;

import cn.allbs.allbsjwt.entity.cm.CmEnterpriseEntity;
import cn.allbs.allbsjwt.service.cm.CmEnterpriseService;
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
 * 企业信息表(cm_enterprise)表控制层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:55
 */
@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/cm/enterprise")
@Api(value = "cmEnterprise", tags = "企业信息表 管理")
public class CmEnterpriseController {

    /**
     * 服务对象
     */
    private final CmEnterpriseService cmEnterpriseService;

    /**
     * 分页查询
     *
     * @param page               分页对象
     * @param cmEnterpriseEntity 企业信息表
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
//    @PreAuthorize("@pms.hasPermission('cmEnterprise_list')")
    public R getCmEnterprisePage(Page page, CmEnterpriseEntity cmEnterpriseEntity) {
        return R.ok(this.cmEnterpriseService.page(page, Wrappers.query(cmEnterpriseEntity)));
    }


    /**
     * 通过id查询 企业信息表
     *
     * @param id 主键
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('cmEnterprise_list')")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(this.cmEnterpriseService.getById(id));
    }

    /**
     * 新增 企业信息表
     *
     * @param cmEnterpriseEntity 企业信息表
     * @return R
     */
    @ApiOperation(value = "新增企业信息表", notes = "新增企业信息表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('cmEnterprise_save')")
    public R save(@RequestBody CmEnterpriseEntity cmEnterpriseEntity) {
        return R.ok(this.cmEnterpriseService.save(cmEnterpriseEntity));
    }

    /**
     * 修改企业信息表
     *
     * @param cmEnterpriseEntity 企业信息表
     * @return R
     */
    @ApiOperation(value = "修改企业信息表", notes = "修改企业信息表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('cmEnterprise_update')")
    public R updateById(@RequestBody CmEnterpriseEntity cmEnterpriseEntity) {
        return R.ok(this.cmEnterpriseService.updateById(cmEnterpriseEntity));
    }

    /**
     * 通过id 删除企业信息表
     *
     * @param id 主键
     * @return R
     */
    @PreAuthorize("@pms.hasPermission('cmEnterprise_delete')")
    @ApiOperation(value = "通过id 删除企业信息表", notes = "通过id 删除企业信息表")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable("id") Long id) {
        return R.ok(this.cmEnterpriseService.removeById(id));
    }

}
