package cn.allbs.allbsjwt.controller.sys;

import cn.allbs.allbsjwt.entity.sys.SysUserEntity;
import cn.allbs.allbsjwt.service.sys.SysUserService;
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
 * 用户表(sys_user)表控制层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:59
 */
@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/sys/user")
@Api(value = "sysUser", tags = "用户表 管理")
public class SysUserController {

    /**
     * 服务对象
     */
    private final SysUserService sysUserService;

    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param sysUserEntity 用户表
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('sysUser_list')")
    public R getSysUserPage(Page page, SysUserEntity sysUserEntity) {
        return R.ok(this.sysUserService.page(page, Wrappers.query(sysUserEntity)));
    }


    /**
     * 通过id查询 用户表
     *
     * @param userId 主键ID
     * @return R
     */
    @ApiOperation(value = "通过userId查询", notes = "通过userId查询")
    @GetMapping("/{userId}")
    @PreAuthorize("@pms.hasPermission('sysUser_list')")
    public R getById(@PathVariable("userId") Integer userId) {
        return R.ok(this.sysUserService.getById(userId));
    }

    /**
     * 新增 用户表
     *
     * @param sysUserEntity 用户表
     * @return R
     */
    @ApiOperation(value = "新增用户表", notes = "新增用户表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sysUser_save')")
    public R save(@RequestBody SysUserEntity sysUserEntity) {
        return R.ok(this.sysUserService.save(sysUserEntity));
    }

    /**
     * 修改用户表
     *
     * @param sysUserEntity 用户表
     * @return R
     */
    @ApiOperation(value = "修改用户表", notes = "修改用户表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sysUser_update')")
    public R updateById(@RequestBody SysUserEntity sysUserEntity) {
        return R.ok(this.sysUserService.updateById(sysUserEntity));
    }

    /**
     * 通过userId 删除用户表
     *
     * @param userId 主键ID
     * @return R
     */
    @PreAuthorize("@pms.hasPermission('sysUser_delete')")
    @ApiOperation(value = "通过userId 删除用户表", notes = "通过userId 删除用户表")
    @DeleteMapping("/{userId}")
    public R removeById(@PathVariable("userId") Integer userId) {
        return R.ok(this.sysUserService.removeById(userId));
    }

}
