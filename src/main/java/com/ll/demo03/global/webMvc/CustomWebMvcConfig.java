package com.ll.demo03.global.webMvc;

import com.ll.demo03.global.AppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
//                        "https://cdpn.io", // codepen.io에서 제공하는 프록시 서버 주소 추가(cors 오류 해결 위해서) 요청 허용
//                        "http://localhost:5173" // 프론트엔드 개발서버 주소 추가(cors 오류 해결 위해서) 요청 허용
                        AppConfig.getSiteFrontUrl()
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}