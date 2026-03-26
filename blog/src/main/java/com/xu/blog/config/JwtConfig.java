package com.xu.blog.config;

import com.xu.common.utils.JWTUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "blog.jwt")
public class JwtConfig {

    private String secret;
    private Long expireMs;

    @PostConstruct
    public void init() {
        JWTUtil.configure(secret, expireMs);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpireMs() {
        return expireMs;
    }

    public void setExpireMs(Long expireMs) {
        this.expireMs = expireMs;
    }
}
