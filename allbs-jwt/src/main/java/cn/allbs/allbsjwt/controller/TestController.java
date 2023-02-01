package cn.allbs.allbsjwt.controller;

import cn.allbs.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类 TestController
 * </p>
 *
 * @author ChenQi
 * @since 2022/12/26 15:26
 */
@Tag(name = "权限系统测试controller", description = "TestController")
@RequestMapping("test")
@RestController
public class TestController {


    @Operation(summary = "成功的方法")
    @Parameters({
//            @Parameter(name = "name", description = "这是名称的注释", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("success")
    public R success() {
        return R.ok("数据查询成功");
    }

    @Operation(summary = "失败的方法")
    @GetMapping("fail")
    public R fail() {
        return R.fail("数据查询失败");
    }
}
