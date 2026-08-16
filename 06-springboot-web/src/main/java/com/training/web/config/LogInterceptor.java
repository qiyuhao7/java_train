package com.training.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器：记录每个请求的耗时
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
        log.info("请求: {} {}", request.getMethod(), request.getRequestURI());
        return true;  // false 则中断请求
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        Object start = request.getAttribute(START_TIME);
        if (start instanceof Long) {
            long cost = System.currentTimeMillis() - (Long) start;
            log.info("响应: {} {}ms", response.getStatus(), cost);
        }
        if (ex != null) {
            log.error("请求异常: {}", request.getRequestURI(), ex);
        }
    }
}
