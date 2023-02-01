package cn.allbs.allbsjwt.controller.sys;

import cn.allbs.allbsjwt.entity.sys.SysRoleEntity;
import cn.allbs.allbsjwt.service.sys.SysRoleService;
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
 * 系统角色表(sys_role)表控制层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/sys/role")
@Api(value = "sysRole", tags = "系统角色表 管理")
public class SysRoleController {

    /**
     * 服务对象
     */
    private final SysRoleService sysRoleService;

    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param sysRoleEntity 系统角色表
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('sysRole_list')")
    public R getSysRolePage(Page page, SysRoleEntity sysRoleEntity) {
        return R.ok(this.sysRoleService.page(page, Wrappers.query(sysRoleEntity)));
    }


    /**
     * 通过id查询 系统角色表
     *
     * @param roleId $pk.comment
     * @return R
     */
    @ApiOperation(value = "通过roleId查询", notes = "通过roleId查询")
    @GetMapping("/{roleId}")
    @PreAuthorize("@pms.hasPermission('sysRole_list')")
    public R getById(@PathVariable("roleId") Integer roleId) {
        return R.ok(this.sysRoleService.getById(roleId));
    }

    /**
     * 新增 系统角色表
     *
     * @param sysRoleEntity 系统角色表
     * @return R
     */
    @ApiOperation(value = "新增系统角色表", notes = "新增系统角色表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sysRole_save')")
    public R save(@RequestBody SysRoleEntity sysRoleEntity) {
        return R.ok(this.sysRoleService.save(sysRoleEntity));
    }

    /**
     * 修改系统角色表
     *
     * @param sysRoleEntity 系统角色表
     * @return R
     */
    @ApiOperation(value = "修改系统角色表", notes = "修改系统角色表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sysRole_update')")
    public R updateById(@RequestBody SysRoleEntity sysRoleEntity) {
        return R.ok(this.sysRoleService.updateById(sysRoleEntity));
    }

    /**
     * 通过roleId 删除系统角色表
     *
     * @param roleId $pk.comment
     * @return R
     */
    @PreAuthorize("@pms.hasPermission('sysRole_delete')")
    @ApiOperation(value = "通过roleId 删除系统角色表", notes = "通过roleId 删除系统角色表")
    @DeleteMapping("/{roleId}")
    public R removeById(@PathVariable("roleId") Integer roleId) {
        return R.ok(this.sysRoleService.removeById(roleId));
    }

}
