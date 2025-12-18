package com.xphr.reporting.ms.config;

import com.xphr.reporting.ms.controller.interceptor.TrackingRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TrackingRequestInterceptor trackingRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(trackingRequestInterceptor);
    }
}
