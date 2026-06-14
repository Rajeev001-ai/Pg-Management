package com.major.pgmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/",
								"/about",
								"/features",
								"/contact",
								"/login",
								"/register",
								"/css/**",
								"/js/**",
								"/images/**",
								"/uploads/**",
								"/error/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/register").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/owner/**").hasRole("OWNER")
						.requestMatchers("/tenant/**").hasRole("TENANT")
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.successHandler(roleBasedAuthenticationSuccessHandler())
						.failureUrl("/login?error=true")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout=true")
						.permitAll())
				.exceptionHandling(exception -> exception
						.accessDeniedPage("/error/access-denied"))
				.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationSuccessHandler roleBasedAuthenticationSuccessHandler() {
		return (request, response, authentication) -> {
			boolean isAdmin = authentication.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
			boolean isOwner = authentication.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals("ROLE_OWNER"));

			if (isAdmin) {
				response.sendRedirect("/admin/dashboard");
			} else if (isOwner) {
				response.sendRedirect("/owner/dashboard");
			} else {
				response.sendRedirect("/tenant/dashboard");
			}
		};
	}
}
