package com.dodo.ai_trader.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口都允许跨域
                .allowedOriginPatterns("*") // 允许任何来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许任何请求头
                .allowCredentials(true) // 允许携带认证信息
                .maxAge(3600); // 预检请求的有效期（秒）

    }
}
