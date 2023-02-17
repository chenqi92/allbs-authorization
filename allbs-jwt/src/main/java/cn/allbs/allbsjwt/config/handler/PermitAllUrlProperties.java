package cn.allbs.allbsjwt.config.handler;

import cn.allbs.allbsjwt.config.annotation.IgnoreUri;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类 IgnoreUrlsHandler
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/1 14:36
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("!'${security.ignore-urls}'.isEmpty()")
@ConfigurationProperties(prefix = "security")
public class PermitAllUrlProperties implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private final WebApplicationContext applicationContext;

    @Getter
    @Setter
    private List<String> ignoreUrls = new ArrayList<>();

    @Getter
    private final Map<HttpMethod, List<String>> ignoreUrlsMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpMethod[] httpMethods = HttpMethod.values();
        for (HttpMethod httpMethod : httpMethods) {
            ignoreUrlsMap.put(httpMethod, new ArrayList<>());
        }
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        for (RequestMappingInfo info : map.keySet()) {
            HandlerMethod handlerMethod = map.get(info);

            // 获取类注解
            IgnoreUri controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), IgnoreUri.class);

            // 获取方法注解
            if (controller == null) {
                IgnoreUri method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), IgnoreUri.class);
                Optional.ofNullable(method).ifPresent(inner -> {
                    assert info.getPathPatternsCondition() != null;
                    info.getPathPatternsCondition().getPatterns()
                            .forEach(url -> this.filterPath(url.getPatternString(), info, map));
                });
                continue;
            }

            Class<?> beanType = handlerMethod.getBeanType();
            Method[] methods = beanType.getDeclaredMethods();
            Method method = handlerMethod.getMethod();
            if (ArrayUtil.contains(methods, method)) {
                assert info.getPathPatternsCondition() != null;
                info.getPathPatternsCondition().getPatterns()
                        .forEach(url -> this.filterPath(url.getPatternString(), info, map));
            }
        }
    }

    private void filterPath(String url, RequestMappingInfo info, Map<RequestMappingInfo, HandlerMethod> map) {
        List<String> methodList = info.getMethodsCondition().getMethods().stream().map(RequestMethod::name)
                .collect(Collectors.toList());
        String resultUrl = ReUtil.replaceAll(url, PATTERN, "*");
        if (!CollUtil.isEmpty(methodList)) {
            ignoreUrlsMap.get(HttpMethod.resolve(CollUtil.join(methodList, StrUtil.COMMA))).add(resultUrl);
        }
    }

}
