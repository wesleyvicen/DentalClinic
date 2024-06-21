package com.sysmei.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sysmei.dto.LoginDto;
import com.sysmei.dto.UsuarioDto;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Usuario;
import com.sysmei.security.JWTUtil;
import com.sysmei.service.impl.UsuarioServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = RotasKeys.USER)
@Tag(name = "Usuario Controller", description = "Gerenciamento de usuários")
public class UsuarioController {

  @Autowired
  private UsuarioServiceImpl usuarioService;
  
  @Autowired
  private JWTUtil jwtUtil;

  /**
   * Retorna um usuário com base no token JWT.
   * 
   * @param token Token de autorização JWT
   * @return Retorna o usuário encontrado ou uma mensagem de erro
   */
  @Operation(summary = "Retorna um usuário")
  @GetMapping()
  public ResponseEntity<?> find(@Parameter(description = "Token de autorização JWT", required = true) @RequestHeader("Authorization") String token) {
      String loginUsuario = jwtUtil.getUsername(token.substring(7)); // Remove "Bearer " do token
    try {
      Usuario obj = usuarioService.getUsuarioWithLogin(loginUsuario);
      return ResponseEntity.ok().body(obj);
    } catch (ObjectNotFoundException e) {
      return new ResponseEntity<>(
          String.format("Usuario de Login %s Não encontrado, por favor tente um ID diferente.", loginUsuario),
          HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      return new ResponseEntity<>(("Houve algum erro interno, por favor tente mais tarde."),
          HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * Verifica o usuário através do código de verificação.
   * 
   * @param code Código de verificação
   * @return Retorna "verify_success" ou "verify_fail" baseado na verificação do usuário
   */
  @Operation(summary = "Verifica o usuário através do código de verificação")
  @GetMapping(RotasKeys.TOKEN)
  public String verificarUser(@Parameter(description = "Código de verificação", required = true) @RequestParam(name = ParamsKeys.code) String code) {
    if (usuarioService.verificarUser(code)) {
      return "verify_success";
    } else {
      return "verify_fail";
    }
  }

  /**
   * Cria um novo usuário.
   * 
   * @param usuarioDto Dados do novo usuário
   * @return Retorna o novo usuário criado
   */
  @Operation(summary = "Cria um novo usuário")
  @PostMapping()
  public ResponseEntity<?> addConta(@Parameter(description = "Dados do novo usuário", required = true) @RequestBody UsuarioDto usuarioDto) {
    try {
      return new ResponseEntity<>(usuarioService.addUsuario(usuarioDto), HttpStatus.CREATED);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(String.format(
          "Usuario %s já existe no sistema e não pode ser criado, por favor tente um login diferente.",
          usuarioDto.getLogin()), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      return new ResponseEntity<>(("Houve algum erro interno, por favor tente mais tarde."),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Realiza o login do usuário.
   * 
   * @param loginDto Dados de login do usuário
   * @return Retorna os dados da sessão do usuário logado
   */
  @Operation(summary = "Realiza o login do usuário")
  @PostMapping(RotasKeys.LOGIN)
  public ResponseEntity<?> logar(@Parameter(description = "Dados de login do usuário", required = true) @RequestBody LoginDto loginDto) {
    try {
      return new ResponseEntity<>(usuarioService.logar(loginDto), HttpStatus.OK);
    } catch (IllegalArgumentException ex) {
      return new ResponseEntity<>(String.format("Erro: %s", ex.getMessage()),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Faz o upload da imagem do perfil do usuário.
   * 
   * @param file Arquivo de imagem do perfil do usuário
   * @return Retorna o URI da imagem do perfil do usuário
   */
  @Operation(summary = "Faz o upload da imagem do perfil do usuário")
  @PostMapping(RotasKeys.PICTURE)
  public ResponseEntity<Void> uploadProfilePicture(
      @Parameter(description = "Arquivo de imagem do perfil do usuário", required = true) @RequestParam(name = ParamsKeys.FILE) MultipartFile file) {
    URI uri = usuarioService.uploadProfilePicture(file);
    return ResponseEntity.created(uri).build();
  }

  /**
   * Atualiza os dados de um usuário.
   * 
   * @param usuarioDto Dados atualizados do usuário
   * @param token Token de autorização JWT
   * @return Retorna os dados atualizados do usuário
   */
  @Operation(summary = "Atualiza os dados de um usuário")
  @PutMapping()
  public ResponseEntity<UsuarioDto> updateUsuario(@Parameter(description = "Dados atualizados do usuário", required = true) @RequestBody UsuarioDto usuarioDto, 
      @Parameter(description = "Token de autorização JWT", required = true) @RequestHeader("Authorization") String token) {
      String loginUsuario = jwtUtil.getUsername(token.substring(7)); // Remove "Bearer " do token
      UsuarioDto updatedUsuario = usuarioService.updateUsuario(usuarioDto, loginUsuario);
      return ResponseEntity.ok(updatedUsuario);
  }

  /**
   * Processa a solicitação de esquecimento de senha.
   * 
   * @param email Email do usuário
   * @return Retorna mensagem de confirmação do envio do email de redefinição de senha
   */
  @Operation(summary = "Processa a solicitação de esquecimento de senha")
  @PostMapping("/forgot_password")
  public ResponseEntity<?> processForgotPassword(@Parameter(description = "Email do usuário", required = true) @RequestParam String email) {
      usuarioService.sendResetPasswordEmail(email);
      return ResponseEntity.ok("E-mail de redefinição de senha enviado");
  }

  /**
   * Processa a redefinição de senha.
   * 
   * @param token Token de redefinição de senha
   * @param password Nova senha do usuário
   * @return Retorna mensagem de confirmação da redefinição de senha
   */
  @Operation(summary = "Processa a redefinição de senha")
  @PostMapping("/reset_password")
  public ResponseEntity<?> processResetPassword(@Parameter(description = "Token de redefinição de senha", required = true) @RequestParam String token, 
      @Parameter(description = "Nova senha do usuário", required = true) @RequestParam String password) {
      usuarioService.resetPassword(token, password);
      return ResponseEntity.ok("Senha redefinida com sucesso");
  }
}
