package com.vanpine.mcpclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径
                .allowedOrigins("*") // 允许所有源（生产环境建议指定具体域名）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .allowedHeaders("*") // 允许所有头部
                .allowCredentials(false) // 不允许携带凭证（如 Cookie）
                .maxAge(3600); // 预检请求缓存时间（秒）
    }
}
