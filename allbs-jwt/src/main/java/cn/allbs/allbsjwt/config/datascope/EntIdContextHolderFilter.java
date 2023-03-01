package cn.allbs.allbsjwt.config.datascope;

import cn.allbs.allbsjwt.config.constant.CommonConstants;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 类 EntIdContextHolderFilter
 * </p>
 *
 * @author ChenQi
 * @since 2023/3/1 10:45
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EntIdContextHolderFilter extends GenericFilterBean {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String entIdListStr = request.getHeader(CommonConstants.ENT_ID_LIST);

        if (StrUtil.isNullOrUndefined(entIdListStr)) {
            CurrentEntIdSearchContextHolder.clear();
        } else {
            Set<Long> entIdList = Convert.toSet(Long.class, entIdListStr);
            CurrentEntIdSearchContextHolder.setEntIdList(entIdList);
            log.debug("获取header中的企业列表为:{}", entIdList);
        }

        filterChain.doFilter(request, response);
        CurrentEntIdSearchContextHolder.clear();
    }
}
