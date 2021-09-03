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

@RestController
@RequestMapping(value = RotasKeys.AUTH)
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;

	
	/**
	 * 
	 * @param response
	 * @return
	 */
	@PostMapping(RotasKeys.REFRESH_TOKEN)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UsuarioServiceImpl.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
}
