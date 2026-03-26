package com.xu.timesheet.config;

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
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Token鉴权拦截器
 */
@Slf4j
@Component
public class TokenHandlerAdapter implements HandlerInterceptor {

    private static final String TOKEN_SPLIT = "##";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        try {
            String token = resolveToken(request);
            if (StringUtils.isBlank(token)) {
                sendError(response, 401, "未登录，请先登录");
                return false;
            }

            Map<String, Claim> map = JWTUtil.analysisToken(token);
            if (map.containsKey("user")) {
                String tokenStr = map.get("user").asString();
                UserToken userToken = JSON.parseObject(tokenStr, UserToken.class);
                UserContext.setCurrentUser(userToken);
                request.setAttribute("currentAccount", userToken != null ? userToken.getAccount() : null);
                return true;
            }

            sendError(response, 401, "Token解析失败");
            return false;
        } catch (TokenExpiredException e) {
            sendError(response, 401, TokenEnum.ERROR_TOKEN_OVERDUE.getMessage());
            return false;
        } catch (JWTDecodeException e) {
            sendError(response, 401, TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
            return false;
        } catch (SignatureVerificationException e) {
            sendError(response, 401, TokenEnum.ERROR_TOKEN_SIGN.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Token验证异常", e);
            sendError(response, 401, "Token验证失败");
            return false;
        }
    }

    /**
     * 兼容 Bearer Token 和旧版 Token 头
     */
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

    /**
     * 解包旧版拼接签名 Token
     */
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    /**
     * 输出统一的鉴权失败响应
     */
    private void sendError(HttpServletResponse response, int status, String message) {
        try {
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":" + status + ",\"codeMessage\":\"" + message + "\"}");
        } catch (Exception e) {
            log.error("发送错误响应失败", e);
        }
    }
}
