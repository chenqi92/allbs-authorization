package cn.allbs.allbsjwt.controller;

import cn.allbs.allbsjwt.config.annotation.IgnoreUri;
import cn.allbs.allbsjwt.config.dto.UserInfo;
import cn.allbs.allbsjwt.service.sys.SysUserService;
import cn.allbs.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类 TestController
 * </p>
 *
 * @author ChenQi
 * @since 2022/12/26 15:26
 */
@Slf4j
@Tag(name = "权限系统测试controller", description = "TestController")
@RequestMapping("test")
@RestController
public class TestController {

    @Resource
    private SysUserService sysUserService;


    @Operation(summary = "成功的方法")
    @Parameters({
//            @Parameter(name = "name", description = "这是名称的注释", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @IgnoreUri
    @GetMapping("success")
    public R<String> success() {
        return R.ok("数据查询成功");
    }

    @Operation(summary = "失败的方法")
    @GetMapping("fail")
    public R<String> fail() {
        return R.fail("数据查询失败");
    }

    /**
     * 同Url不同请求方法的拦截测试
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public R<String> getId(@PathVariable("id") Long id) {
        return R.ok("这是delete请求的id" + id);
    }

    /**
     * 同Url不同请求方法的拦截测试
     *
     * @param id
     * @return
     */
    @PostMapping("{id}")
    @IgnoreUri
    public R<String> postId(@PathVariable("id") Long id) {
        return R.ok("这是post请求的id" + id);
    }

    @GetMapping("testPms")
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public R<String> testPms() {
        return R.ok();
    }

    @GetMapping("testPms1")
    @PreAuthorize("@pms.hasPermission('sys_test')")
    public R<String> testPms1() {
        return R.ok();
    }

    /**
     * 权限控制 没有权限方法依然执行但是不会返回
     *
     * @return
     */
    @GetMapping("testPostAuth")
    @PostAuthorize("hasAnyAuthority('sys_test')")
    public String postAuthTest() {
        log.info("test.........");
        return "auth test";
    }

    /**
     * 对结果进行过滤
     *
     * @return
     */
    @GetMapping("postFilter")
    @PreAuthorize("hasAnyAuthority('sys_dict_add')")
    @PostFilter("filterObject.longValue() != 1")
    public List<Long> postFilter() {
        List<Long> list = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        return list;
    }

    /**
     * 对请求参数进行过滤
     *
     * @param pres
     * @return
     */
    @GetMapping("preFiler")
    @PreAuthorize("hasAnyAuthority('sys_dict_add')")
    @PreFilter(filterTarget = "pres", value = "!'admin'.equals(filterObject.toString())")
    public List<String> preFilter(@RequestParam("pres") List<String> pres) {
        log.info("接收到的数据" + pres);
        return pres;
    }

    @GetMapping("getUserInfo")
    @PostAuthorize("returnObject.sysUser.username == principal.username")
    public UserInfo getUserInfo(@RequestParam("userName") String userName) {
        return sysUserService.findUserInfoByUserName(userName);
    }
}
