/**
 *
 */
package com.sysmei.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sysmei.keys.Keys;
import com.sysmei.security.JWTUtil;
import com.sysmei.security.UserSS;
import com.sysmei.service.UsuarioService;

@RestController
@RequestMapping(value = Keys.AUTH)
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;

	
	/**
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = Keys.REFRESH_TOKEN, method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UsuarioService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
}