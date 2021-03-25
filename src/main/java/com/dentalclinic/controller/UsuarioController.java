package com.dentalclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dentalclinic.dto.LoginDto;
import com.dentalclinic.dto.UsuarioDto;
import com.dentalclinic.exceptions.ObjectNotFoundException;
import com.dentalclinic.model.Usuario;
import com.dentalclinic.service.UsuarioService;

@RestController
@RequestMapping(value = "/user")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
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

	@PostMapping()
	public ResponseEntity<?> addConta(@RequestBody UsuarioDto usuarioDto) {
		try {

			return new ResponseEntity<>(usuarioService.addUsuario(usuarioDto), HttpStatus.CREATED);

		} catch (IllegalStateException e) {
			return new ResponseEntity<>(String.format(
					"Usuario %s já existe no sistema e não pode ser criado, por favor tente um login diferente.",
					usuarioDto.getLogin()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<>((String.format(
					"Houve algum erro intento no cadasto e usuario %s não pode ser criado, por favor tente mais tarde.",
					usuarioDto.getLogin())), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/login")
	public ResponseEntity<?> logar(@RequestBody LoginDto loginDto) {
		try {
			return new ResponseEntity<>(usuarioService.logar(loginDto), HttpStatus.OK);

		} catch (IllegalArgumentException ex) {
			return new ResponseEntity<>(String.format(
					"Erro: %s",
					ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

}
