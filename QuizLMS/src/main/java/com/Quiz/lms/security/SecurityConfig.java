package com.Quiz.lms.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	//로그인 유지 관련
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
    private DataSource dataSource;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
	    if (authenticationManager == null) {
	        throw new IllegalStateException("AuthenticationManager is not configured properly");
	    }
	    return authenticationManager;
	}


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
	
	//보안 설정
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
                    .requestMatchers("/api/members/register", "/api/members/check", "register", "category", "join", "/login", "/css/**"
                            , "/js/**", "/api/chat", "/api/**", "/quiz/**").permitAll()
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

    
    //CORS 관련 리액트 도메인 요청 허용하기
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 리액트 앱 주소
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);  // 데이터베이스 설정
        return tokenRepository;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}