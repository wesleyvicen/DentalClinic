package com.dentalclinic.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dentalclinic.security.JWTAuthenticationFilter;
import com.dentalclinic.security.JWTAuthorizationFilter;
import com.dentalclinic.security.JWTUtil;
import com.dentalclinic.service.EmailService;
import com.dentalclinic.service.SmtpEmailService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTUtil jwtUtil;

	/***
	 * Array com os endpoints liberação sem precisar de autenticação
	 */
	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };

	private static final String[] SWAGGER_WHITELIST = { "/v2/api-docs", "/swagger-resources", "/swagger-resources/**",
			"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**" };

	/***
	 * Array com os endpoints liberação para recuperar dados sem autenticação
	 */
	private static final String[] PUBLIC_MATCHERS_GET = {"/agenda/public"};

	private static final String[] PUBLIC_MATCHERS_POST = { "/user", "/user/login/**" };

	// Basic Auth com usuario em memoria e sem criptografia
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("user").password("{noop}123").roles("ROLE_ADMIN");
//	}

	/***
	 * Método para autorizar acesso aos endpoints que precisam de autenticação, sem
	 * configuração de ataque CSRF pois o sistema é stateless e sem criar seção de
	 * usuário.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		http.csrf().disable();
		http.cors().and().authorizeRequests().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
				.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll().antMatchers(PUBLIC_MATCHERS).permitAll()
				.antMatchers(SWAGGER_WHITELIST).permitAll().anyRequest().authenticated();
//				.antMatchers(HttpMethod.POST, "/palavrashome/**").hasAnyRole("ADMIN");

		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	/***
	 * Método que configura as restrições de CORS com restrições básicas
	 * 
	 * @return
	 */

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:4200/**",
				"https://sysmei.netlify.app", "https://sysmei.netlify.app/**", "https://sysmei.com",
				"https://www.sysmei.com", "https://sysmei.com/**", "https://www.sysmei.com/**",
				"https://dev-sysmei.netlify.app", "https://dev-sysmei.netlify.app/**", "https://dev.sysmei.com/**", 
				"https://dev.sysmei.com", "https://api2.sysmei.com/**", "https://api2.sysmei.com"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "HEAD"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
		return source;
	}
	/*
	 * @Bean CorsConfigurationSource corsConfigurationSource() { final
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); CorsConfiguration corsConfiguration = new
	 * CorsConfiguration(); corsConfiguration.addAllowedMethod(HttpMethod.GET);
	 * corsConfiguration.addAllowedMethod(HttpMethod.POST);
	 * corsConfiguration.addAllowedMethod(HttpMethod.PUT);
	 * corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
	 * corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
	 * corsConfiguration.addAllowedMethod(HttpMethod.HEAD);
	 * corsConfiguration.addAllowedOrigin("**");
	 * corsConfiguration.addAllowedOrigin("/**");
	 * source.registerCorsConfiguration("/**",
	 * corsConfiguration.applyPermitDefaultValues()); return source; }
	 */

	/***
	 * Método que encripta a senha
	 * 
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}