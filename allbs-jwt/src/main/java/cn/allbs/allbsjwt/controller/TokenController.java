package cn.allbs.allbsjwt.controller;

import cn.allbs.allbsjwt.config.dto.LoginDTO;
import cn.allbs.common.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 类 TokenHandler
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/1 16:00
 */
@Tag(name = "token相关接口", description = "TokenHandler")
@RequestMapping("token")
@RestController
public class TokenController {

    /**
     * 登录
     *
     * @return R
     */
    @Operation(summary = "用户登录")
    @Parameters({
            @Parameter(name = "username", description = "用户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功")
    })
    @PostMapping("login")
    public R login(@RequestBody LoginDTO loginDTO) {
        return R.ok();
    }

    /**
     * 退出
     *
     * @return R
     */
    @Operation(summary = "用户登出")
    @Parameters({
            @Parameter(name = "Authorization", description = "token", required = true, in = ParameterIn.HEADER),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "退出成功")
    })
    @DeleteMapping("logout")
    public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        return R.ok();
    }
}
