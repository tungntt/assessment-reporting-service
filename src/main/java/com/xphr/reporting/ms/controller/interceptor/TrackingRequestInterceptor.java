package com.xphr.reporting.ms.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class TrackingRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Received the request with {} Method - URI {} - Remote Address {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
        request.setAttribute("startTime", System.currentTimeMillis());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception exception) throws Exception {

        if (exception != null) {
            log.error("Exception occurred while processing the request", exception);
        }

        Long startTime = (Long) request.getAttribute("startTime");

        log.info("Complete the request with {} Method - URI {} - Status: {} - Duration: {} ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                System.currentTimeMillis() - startTime);

        HandlerInterceptor.super.afterCompletion(request, response, handler, exception);
    }
}
