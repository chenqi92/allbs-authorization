package cn.allbs.allbsjwt.config.constant;

/**
 * 接口 SecurityConstant
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/1 16:31
 */
public interface SecurityConstant {

    /**
     * 签名
     */
    String SIGNING_KEY = "chenqi";

    /**
     * 正常用户
     */
    String STATUS_NORMAL = "0";

    /**
     * 冻结
     */
    String STATUS_LOCK = "9";

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";

    /**
     * Bearer
     */
    String BEARER_TYPE = "Bearer ";

    /**
     * token
     */
    String TOKEN = "token";
}
