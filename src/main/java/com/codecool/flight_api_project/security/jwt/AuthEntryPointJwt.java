package com.codecool.flight_api_project.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 401 responses from the security filter chain (missing, invalid or expired token). Delegates to
 * Spring MVC's exception resolution so the response is rendered by {@code GlobalExceptionHandler}
 * as {@code application/problem+json}, like every other error.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver exceptionResolver;

    public AuthEntryPointJwt(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        exceptionResolver.resolveException(request, response, null, authException);
    }
}
