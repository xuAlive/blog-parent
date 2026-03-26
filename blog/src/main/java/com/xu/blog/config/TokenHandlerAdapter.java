package com.xu.blog.config;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xu.common.context.UserContext;
import com.xu.common.enums.TokenEnum;
import com.xu.common.param.UserToken;
import com.xu.common.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Configuration
public class TokenHandlerAdapter implements HandlerInterceptor {

    private final static String TOKEN_SPLIT = "##";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 预检请求直接放行，不验证 token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        try {
            String token = resolveToken(request);
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException(TokenEnum.IS_TOKEN_NULL.getMessage());
            }

            /**
             * 解析token并放入上下文
             */
            Map<String, Claim> map = JWTUtil.analysisToken(token);
            if (map.containsKey("user")) {
                String tokenStr = map.get("user").asString();
                UserToken userToken = JSON.parseObject(tokenStr, UserToken.class);

                UserContext.setCurrentUser(userToken);
                request.setAttribute("currentAccount", userToken != null ? userToken.getAccount() : null);

                return true;
            }
        } catch (TokenExpiredException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_OVERDUE.getMessage());
        } catch (JWTDecodeException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_SIGN.getMessage());
        }
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(TokenEnum.HEADER_AUTHORIZATION_KEY.getCode());
        if (StringUtils.isNotBlank(authorization)
                && authorization.startsWith(TokenEnum.BEARER_PREFIX.getCode())) {
            return authorization.substring(TokenEnum.BEARER_PREFIX.getCode().length()).trim();
        }

        String legacyToken = request.getHeader(TokenEnum.HEADER_TOKEN_KEY.getCode());
        if (StringUtils.isBlank(legacyToken)) {
            return null;
        }
        return unwrapLegacyToken(legacyToken);
    }

    private String unwrapLegacyToken(String token) {
        String[] parts = token.split(TOKEN_SPLIT);
        if (parts.length != 2 || StringUtils.isBlank(parts[0]) || StringUtils.isBlank(parts[1])) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
        }

        long now = System.currentTimeMillis();
        int currentMinute = (int) ((now - (now % (1000 * 60))) / 1000);
        long currentMinuteMillis = currentMinute * 1000L;
        long previousMinuteMillis = currentMinuteMillis - 1000 * 60L;

        String currentSign = DigestUtils.md5DigestAsHex((TokenEnum.LEGACY_KEY.getCode() + currentMinuteMillis).getBytes()).toUpperCase();
        if (parts[1].equals(currentSign)) {
            return parts[0];
        }

        String previousSign = DigestUtils.md5DigestAsHex((TokenEnum.LEGACY_KEY.getCode() + previousMinuteMillis).getBytes()).toUpperCase();
        if (parts[1].equals(previousSign)) {
            return parts[0];
        }

        throw new RuntimeException(TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
    }

    /**
     * 请求完成后清理ThreadLocal，避免内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
