package com.major.PgManagement.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_OWNER")) {
            response.sendRedirect("/owner/dashboard");
        } else {
            response.sendRedirect("/tenant/dashboard");
        }
    }
}

