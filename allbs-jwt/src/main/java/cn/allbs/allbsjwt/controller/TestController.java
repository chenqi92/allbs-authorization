package cn.allbs.allbsjwt.controller;

import cn.allbs.allbsjwt.config.annotation.IgnoreUri;
import cn.allbs.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("{id}")
    public R<String> getId(@PathVariable("id") Long id) {
        return R.ok("这是delete请求的id" + id);
    }

    @PostMapping("{id}")
    @IgnoreUri
    public R<String> postId(@PathVariable("id") Long id) {
        return R.ok("这是post请求的id" + id);
    }
}
