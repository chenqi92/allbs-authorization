package cn.allbs.allbsjwt.config.datascope;

import cn.allbs.allbsjwt.config.annotation.DataScope;
import org.springframework.aop.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 类 DataScopeInitConfig
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/28 18:13
 */
@Configuration
public class DataScopeInitConfig {

    @Bean
    public Advisor generateAllDataScopeAdvisor() {
        DataScopeAnnotationIntercept intercept = new DataScopeAnnotationIntercept();
        DataScopeAnnotationAdvisor advisor = new DataScopeAnnotationAdvisor(intercept, DataScope.class);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }
}
