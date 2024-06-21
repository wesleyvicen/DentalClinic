package com.sysmei.config;

import com.sysmei.security.JWTAuthenticationFilter;
import com.sysmei.security.JWTAuthorizationFilter;
import com.sysmei.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] PUBLIC_MATCHERS = {"/h2-console/**", "/user/token"};
    private static final String[] SWAGGER_WHITELIST =
        {"/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"};
    private static final String[] PUBLIC_MATCHERS_GET = {"/agenda/public"};
    private static final String[] PUBLIC_MATCHERS_POST = {
        "/user", 
        "/user/login/**", 
        "/user/forgot_password", 
        "/user/reset_password"
    };
    private static final String[] PUBLIC_MATCHERS_PUT = {
            "/user"
        };

    @Autowired
    private Environment env;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.csrf().disable()
            .cors().and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
            .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            .antMatchers(HttpMethod.PUT, PUBLIC_MATCHERS_PUT).permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .antMatchers(SWAGGER_WHITELIST).permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(new JWTAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JWTAuthorizationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200",
            "http://localhost:4200/**", "https://sysmei.netlify.app", "https://sysmei.netlify.app/**",
            "https://sysmei.com", "https://www.sysmei.com", "https://sysmei.com/**",
            "https://www.sysmei.com/**", "https://dev-sysmei.netlify.app",
            "https://dev-sysmei.netlify.app/**", "https://dev.sysmei.com/**", "https://dev.sysmei.com",
            "https://api2.sysmei.com/**", "https://api2.sysmei.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "HEAD"));
        configuration.setAllowCredentials(true); // Permitir credenciais
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
