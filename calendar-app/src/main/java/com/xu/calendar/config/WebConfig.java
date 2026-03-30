package com.xu.calendar.config;

import com.xu.common.config.BaseTokenWebConfig;
import com.xu.common.config.CommonTokenHandlerInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;

/**
 * Web 配置类
 * 配置拦截器
 */
@Configuration
public class WebConfig extends BaseTokenWebConfig {

    public WebConfig(CommonTokenHandlerInterceptor tokenHandlerInterceptor) {
        super(tokenHandlerInterceptor);
    }

    @Override
    protected String[] getIncludePatterns() {
        return new String[]{"/calendar/**"};
    }

    @Override
    protected List<String> getExcludePatterns() {
        return List.of("/calendar/health");
    }
}
