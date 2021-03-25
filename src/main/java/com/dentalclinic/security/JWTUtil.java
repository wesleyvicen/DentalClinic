/**
 * 
 */
package com.dentalclinic.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dentalclinic.dto.TokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Wesley Vicente
 *
 *         Clase JWTUtil para gerar o token do usuario
 */

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public TokenDto getToken(String login) {
		TokenDto tokenDto = new TokenDto();
		long currentTimeMillis = System.currentTimeMillis();
		String token = generateToken(login, currentTimeMillis);
		tokenDto.setHoraInicio(Long.toString(currentTimeMillis));
		tokenDto.setHoraFim(Long.toString(currentTimeMillis + expiration));
		tokenDto.setToken("Bearer " + token);
		return tokenDto;
	}
	
	public String generateToken(String login) {
		return Jwts.builder().setSubject(login).setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}
	
	public String generateToken(String login, long currentTimeMillis) {
		return Jwts.builder().setSubject(login).setExpiration(new Date(currentTimeMillis + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}

	/***
	 * 
	 * @param token
	 * @return
	 */
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	/***
	 * Recupera as revindicações através do token passado
	 * 
	 * @param token
	 * @return
	 */
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}

	}

	/***
	 * Retorna o usuário de acordo com token
	 * 
	 * @param token
	 * @return
	 */
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

}
