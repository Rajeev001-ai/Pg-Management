package com.major.PgManagement.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomepage() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"))) {
            return "redirect:/owner/dashboard";
        }

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TENANT"))) {
            return "redirect:/tenant/dashboard";
        }

        return "redirect:/login?error";
    }
}
