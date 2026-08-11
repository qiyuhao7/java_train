package com.training.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置：注册拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TraceInterceptor traceInterceptor;

    public WebConfig(TraceInterceptor traceInterceptor) {
        this.traceInterceptor = traceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/health");
    }
}
