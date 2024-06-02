/**
 *
 */
package com.sysmei.controller;

import com.sysmei.keys.RotasKeys;
import com.sysmei.security.JWTUtil;
import com.sysmei.security.UserSS;
import com.sysmei.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(value = RotasKeys.AUTH)
@Tag(name = "Auth Resource", description = "Recursos de autenticação")
public class AuthResource {

  @Autowired
  private JWTUtil jwtUtil;

  /**
   * Atualiza o token JWT.
   * 
   * @param response Resposta HTTP para adicionar o novo token
   * @return ResponseEntity com status 204 (No Content)
   */
  @Operation(summary = "Atualiza o token JWT")
  @PostMapping(RotasKeys.REFRESH_TOKEN)
  public ResponseEntity<Void> refreshToken(@Parameter(description = "Resposta HTTP para adicionar o novo token") HttpServletResponse response) {
    UserSS user = UsuarioServiceImpl.authenticated();
    String token = jwtUtil.generateToken(user.getUsername());
    response.addHeader("Authorization", "Bearer " + token);
    response.addHeader("access-control-expose-headers", "Authorization");
    return ResponseEntity.noContent().build();
  }
}
