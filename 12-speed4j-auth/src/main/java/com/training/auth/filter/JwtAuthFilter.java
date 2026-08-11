package com.training.auth.filter;

import com.training.auth.config.UserContext;
import com.training.auth.config.UserInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * JWT 认证过滤器
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${auth.token.secret:my-secret-key-for-training-at-least-32-bytes}")
    private String secret;

    /** 白名单路径 */
    private static final List<String> WHITELIST = Arrays.asList(
        "/auth/login",
        "/auth/callback",
        "/api/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 白名单放行
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        // 获取 Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeError(response, 401, "未提供认证令牌");
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 验证并解析 Token
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

            // 存入上下文
            UserInfo userInfo = new UserInfo(
                Long.parseLong(claims.getSubject()),
                claims.get("name", String.class),
                claims.get("roles", List.class)
            );
            UserContext.set(userInfo);

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            writeError(response, 401, "令牌已过期，请重新登录");
        } catch (JwtException e) {
            writeError(response, 401, "令牌无效");
        } finally {
            UserContext.clear();  // 防止线程池污染
        }
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
            String.format("{\"code\":%d,\"message\":\"%s\",\"data\":null}", status, message));
    }
}
