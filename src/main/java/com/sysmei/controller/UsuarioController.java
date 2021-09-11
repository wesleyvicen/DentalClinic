package com.sysmei.controller;

import com.sysmei.dto.LoginDto;
import com.sysmei.dto.UsuarioDto;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Usuario;
import com.sysmei.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping(value = RotasKeys.USER)
public class UsuarioController {

  @Autowired
  private UsuarioServiceImpl usuarioService;

  /**
   * @param id
   * @return
   */

  @GetMapping(RotasKeys.ID)
  public ResponseEntity<?> find(@PathVariable Integer id) {
    try {
      Usuario obj = usuarioService.search(id);
      return ResponseEntity.ok().body(obj);
    } catch (ObjectNotFoundException e) {
      return new ResponseEntity<>(
          String.format("Usuario de ID %s Não encontrado, por favor tente um ID diferente.", id),
          HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      return new ResponseEntity<>(("Houve algum erro intento, por favor tente mais tarde."),
          HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * @param code
   * @return
   */

  @GetMapping(RotasKeys.TOKEN)
  public String verificarUser(@RequestHeader(name = ParamsKeys.code) String code) {
    if (usuarioService.verificarUser(code)) {
      return "verify_success";
    } else {
      return "verify_fail";
    }
  }

  /**
   * @param usuarioDto
   * @return
   */
  @PostMapping()
  public ResponseEntity<?> addConta(@RequestBody UsuarioDto usuarioDto) {
    try {

      return new ResponseEntity<>(usuarioService.addUsuario(usuarioDto), HttpStatus.CREATED);

    } catch (IllegalStateException e) {
      return new ResponseEntity<>(String.format(
          "Usuario %s já existe no sistema e não pode ser criado, por favor tente um login diferente.",
          usuarioDto.getLogin()), HttpStatus.NOT_ACCEPTABLE);
    } catch (Exception e) {
      return new ResponseEntity<>(("Houve algum erro intento, por favor tente mais tarde."),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param loginDto
   * @return
   */

  @PostMapping(RotasKeys.LOGIN)
  public ResponseEntity<?> logar(@RequestBody LoginDto loginDto) {
    try {
      return new ResponseEntity<>(usuarioService.logar(loginDto), HttpStatus.OK);

    } catch (IllegalArgumentException ex) {
      return new ResponseEntity<>(String.format("Erro: %s", ex.getMessage()),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param file
   * @return
   */

  @PostMapping(RotasKeys.PICTURE)
  public ResponseEntity<Void> uploadProfilePicture(
      @RequestParam(name = ParamsKeys.FILE) MultipartFile file) {
    URI uri = usuarioService.uploadProfilePicture(file);
    return ResponseEntity.created(uri).build();
  }

}
