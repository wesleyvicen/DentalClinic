/**
 *
 */
package com.sysmei.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wesley Vicente
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  UserDetailsService userDetailsService;
  private JWTUtil jwtUtil;

  /***
   *
   * @param authenticationManager
   * @param jwtUtil
   * @param userDetailsService
   */
  public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
      UserDetailsService userDetailsService) {
    super(authenticationManager);
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  /***
   * Liberar a autorização para o usuário autenticado
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
      if (auth != null) {
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }
    chain.doFilter(request, response);
  }

  /***
   * Gera autorização a partir do token
   *
   * @param request
   * @param substring
   * @return
   */
  private UsernamePasswordAuthenticationToken getAuthentication(String token) {
    if (jwtUtil.tokenValido(token)) {
      String username = jwtUtil.getUsername(token);
      UserDetails user = userDetailsService.loadUserByUsername(username);
      return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
    return null;
  }

}
