package com.cheeus.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity // Spring Security
@Configuration
public class WebSecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        	.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        	.csrf(AbstractHttpConfigurer::disable)
        	.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/").permitAll()
        );


        http
        .authorizeHttpRequests( (authorizeHttpRequests)->authorizeHttpRequests
        		.anyRequest().permitAll()
        		) 
        ; 
        
        return http.build();
    }
	
//  CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
}
