package cn.allbs.allbsjwt.config.constant;

/**
 * 接口 CacheConstant
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/17 15:38
 */
public interface CacheConstant {

    /**
     * 项目名称
     */
    String PRODUCT = "allbs:";

    /**
     * 储存用户信息
     */
    String USER_DETAILS = PRODUCT + "user_details";

    /**
     * 菜单信息缓存
     */
    String MENU_DETAILS = PRODUCT + "menu_details";

    /**
     * token信息
     */
    String CACHE_TOKEN = PRODUCT + "token:";
}
