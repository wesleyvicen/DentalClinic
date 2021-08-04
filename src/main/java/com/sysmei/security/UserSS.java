/**
 *
 */
package com.sysmei.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sysmei.model.Usuario;

import enums.Perfil;

/**
 * @author Wesley Vicente
 *
 *         Classe que implementa a interface UserDetails do Spring Security
 */
public class UserSS implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String login;
	private String senha;
	// falta determinar o perfil do usuário do sistema
	private Collection<? extends GrantedAuthority> authorities;

	public UserSS() {

	}

	public UserSS(Integer id,String login, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao()))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}



	public Integer getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return login;
	}

	/***
	 * Método que contém a regra de negocio para quando o usuário expira
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

    public boolean isEnabled(Usuario user) {
        return user.isEnabled();
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

}
