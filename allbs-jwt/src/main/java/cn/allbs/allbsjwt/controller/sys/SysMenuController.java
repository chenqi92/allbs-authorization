package cn.allbs.allbsjwt.controller.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.allbs.common.utils.R;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;
import cn.allbs.allbsjwt.service.sys.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 菜单权限表(sys_menu)表控制层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/sys/menu")
@Api(value = "sysMenu", tags = "菜单权限表 管理")
public class SysMenuController {

    /**
     * 服务对象
     */
    private final SysMenuService sysMenuService;

    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param sysMenuEntity 菜单权限表
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('sysMenu_list')")
    public R getSysMenuPage(Page page, SysMenuEntity sysMenuEntity) {
        return R.ok(this.sysMenuService.page(page, Wrappers.query(sysMenuEntity)));
    }


    /**
     * 通过id查询 菜单权限表
     *
     * @param menuId 菜单ID
     * @return R
     */
    @ApiOperation(value = "通过menuId查询", notes = "通过menuId查询")
    @GetMapping("/{menuId}")
    @PreAuthorize("@pms.hasPermission('sysMenu_list')")
    public R getById(@PathVariable("menuId") Integer menuId) {
        return R.ok(this.sysMenuService.getById(menuId));
    }

    /**
     * 新增 菜单权限表
     *
     * @param sysMenuEntity 菜单权限表
     * @return R
     */
    @ApiOperation(value = "新增菜单权限表", notes = "新增菜单权限表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sysMenu_save')")
    public R save(@RequestBody SysMenuEntity sysMenuEntity) {
        return R.ok(this.sysMenuService.save(sysMenuEntity));
    }

    /**
     * 修改菜单权限表
     *
     * @param sysMenuEntity 菜单权限表
     * @return R
     */
    @ApiOperation(value = "修改菜单权限表", notes = "修改菜单权限表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sysMenu_update')")
    public R updateById(@RequestBody SysMenuEntity sysMenuEntity) {
        return R.ok(this.sysMenuService.updateById(sysMenuEntity));
    }

    /**
     * 通过menuId 删除菜单权限表
     *
     * @param menuId 菜单ID
     * @return R
     */
    @PreAuthorize("@pms.hasPermission('sysMenu_delete')")
    @ApiOperation(value = "通过menuId 删除菜单权限表", notes = "通过menuId 删除菜单权限表")
    @DeleteMapping("/{menuId}")
    public R removeById(@PathVariable("menuId") Integer menuId) {
        return R.ok(this.sysMenuService.removeById(menuId));
    }

}
