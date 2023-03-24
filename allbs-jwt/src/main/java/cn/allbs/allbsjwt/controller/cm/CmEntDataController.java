package cn.allbs.allbsjwt.controller.cm;

import cn.allbs.allbsjwt.entity.cm.CmEntDataEntity;
import cn.allbs.allbsjwt.service.cm.CmEntDataService;
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
 * 用户数据测试的表(cm_ent_data)表控制层
 *
 * @author chenqi
 * @since 2023-03-01 09:48:54
 */
@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/cm/ent/data")
@Api(value = "cmEntData", tags = "用户数据测试的表 管理")
public class CmEntDataController {

    /**
     * 服务对象
     */
    private final CmEntDataService cmEntDataService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param cmEntDataEntity 用户数据测试的表
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
//    @PreAuthorize("@pms.hasPermission('cmEntData_list')")
    public R getCmEntDataPage(Page page, CmEntDataEntity cmEntDataEntity) {
        return R.ok(this.cmEntDataService.page(page, Wrappers.query(cmEntDataEntity)));
    }


    /**
     * 通过id查询 用户数据测试的表
     *
     * @param id 主键
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('cmEntData_list')")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(this.cmEntDataService.getById(id));
    }

    /**
     * 新增 用户数据测试的表
     *
     * @param cmEntDataEntity 用户数据测试的表
     * @return R
     */
    @ApiOperation(value = "新增用户数据测试的表", notes = "新增用户数据测试的表")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('cmEntData_save')")
    public R save(@RequestBody CmEntDataEntity cmEntDataEntity) {
        return R.ok(this.cmEntDataService.save(cmEntDataEntity));
    }

    /**
     * 修改用户数据测试的表
     *
     * @param cmEntDataEntity 用户数据测试的表
     * @return R
     */
    @ApiOperation(value = "修改用户数据测试的表", notes = "修改用户数据测试的表")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('cmEntData_update')")
    public R updateById(@RequestBody CmEntDataEntity cmEntDataEntity) {
        return R.ok(this.cmEntDataService.updateById(cmEntDataEntity));
    }

    /**
     * 通过id 删除用户数据测试的表
     *
     * @param id 主键
     * @return R
     */
//    @PreAuthorize("@pms.hasPermission('cmEntData_delete')")
    @ApiOperation(value = "通过id 删除用户数据测试的表", notes = "通过id 删除用户数据测试的表")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable("id") Long id) {
        return R.ok(this.cmEntDataService.removeById(id));
    }

}
