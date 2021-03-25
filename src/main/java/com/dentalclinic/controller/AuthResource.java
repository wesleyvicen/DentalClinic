/**
 * 
 */
package com.dentalclinic.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dentalclinic.security.JWTUtil;
import com.dentalclinic.security.UserSS;
import com.dentalclinic.service.UsuarioService;

/**
 * @author renan
 *
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UsuarioService.authenticated();
		//String token = jwtUtil.generateToken(user.getUsername());
		//response.addHeader("Authorization", "Bearer " + token);
		//response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
}