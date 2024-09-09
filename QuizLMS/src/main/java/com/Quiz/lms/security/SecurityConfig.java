package com.Quiz.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http	
            .authenticationProvider(authenticationProvider())
//            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .ignoringRequestMatchers("/api/members/register", "/login", "/logout", "/api/chat")
//            )
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/members/register", "/api/members/check", "join", "/login", "/css/**", "/js/**", "/api/chat").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .successHandler((request, response, authentication) -> {
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\":true}");
                    })
                    .failureHandler((request, response, exception) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"success\":false,\"error\":\"" + exception.getMessage() + "\"}");
                    })
                    .permitAll()
            )
            .logout(logout ->
                logout
	                .logoutUrl("/logout") // 로그아웃 요청 URL
	                .logoutSuccessUrl("/login") // 로그아웃 성공 시 리다이렉트 URL
	                .invalidateHttpSession(true) // 세션 무효화
	                .deleteCookies("JSESSIONID", "remember-me") // 쿠키 삭제
            )
            .rememberMe(rememberMe -> rememberMe
            	    .rememberMeParameter("remember-me") // 기본 remember-me 파라미터
            	    .tokenRepository(persistentTokenRepository()) // 토큰 저장소 설정
            	    .userDetailsService(userDetailsService)
            	    .tokenValiditySeconds(86400) // 1일 유지
        	)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("user")
                .password("{noop}password") // 암호화 처리 필요
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/
}

