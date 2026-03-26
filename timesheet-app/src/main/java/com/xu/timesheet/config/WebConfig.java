package com.xu.timesheet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenHandlerAdapter tokenHandlerAdapter;

    public WebConfig(TokenHandlerAdapter tokenHandlerAdapter) {
        this.tokenHandlerAdapter = tokenHandlerAdapter;
    }

    /**
     * 为工时接口注册 Token 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenHandlerAdapter)
                .addPathPatterns("/timesheet/**")
                .excludePathPatterns("/timesheet/health");
    }
}
