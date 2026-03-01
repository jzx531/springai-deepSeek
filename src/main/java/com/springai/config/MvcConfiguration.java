package com.springai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // ✅ 替换为你前端的实际地址 (例如 Vite 默认是 5173, Vue CLI 是 8080)
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                // ✅ 允许携带凭证 (Cookie, Authorization Headers)
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Content-Type") // 暴露特定头给前端读取
                .maxAge(3600);
    }
}