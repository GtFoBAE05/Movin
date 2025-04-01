package com.imannuel.movin.authenticationservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imannuel.movin.commonservice.dto.response.template.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomEntryPointException implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CommonResponse<?> basicResponse = new CommonResponse<>();
        basicResponse.setSuccess(false);
        basicResponse.setMessage(authException.getMessage());
        basicResponse.setData(null);
        basicResponse.setPagination(null);

        String stringBasicResponse = objectMapper.writeValueAsString(basicResponse);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(stringBasicResponse);
    }
}
