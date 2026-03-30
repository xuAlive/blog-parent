package com.xu.timesheet.config;

import com.xu.common.config.BaseTokenWebConfig;
import com.xu.common.config.CommonTokenHandlerInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;

/**
 * Web MVC 配置
 */
@Configuration
public class WebConfig extends BaseTokenWebConfig {

    public WebConfig(CommonTokenHandlerInterceptor tokenHandlerInterceptor) {
        super(tokenHandlerInterceptor);
    }

    @Override
    protected String[] getIncludePatterns() {
        return new String[]{"/timesheet/**"};
    }

    @Override
    protected List<String> getExcludePatterns() {
        return List.of("/timesheet/health");
    }
}
