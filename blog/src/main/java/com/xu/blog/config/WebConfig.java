package com.xu.blog.config;

import com.xu.blog.interceptor.PermissionInterceptor;
import com.xu.common.config.BaseTokenWebConfig;
import com.xu.common.config.CommonTokenHandlerInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig extends BaseTokenWebConfig {

    private final PermissionInterceptor permissionInterceptor;

    public WebConfig(CommonTokenHandlerInterceptor tokenHandlerInterceptor,
                     PermissionInterceptor permissionInterceptor) {
        super(tokenHandlerInterceptor);
        this.permissionInterceptor = permissionInterceptor;
    }

    @Override
    protected String[] getIncludePatterns() {
        return new String[]{"/blog/**"};
    }

    @Override
    protected List<String> getExcludePatterns() {
        return List.of(
                "/blog/sys/login",
                "/blog/sys/register",
                "/blog/wechat/**"
        );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns(getIncludePatterns())
                .excludePathPatterns(getExcludePatterns());
    }
}
