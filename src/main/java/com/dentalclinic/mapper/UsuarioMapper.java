package com.dentalclinic.mapper;

import com.dentalclinic.dto.UsuarioDto;
import com.dentalclinic.model.Usuario;

public class UsuarioMapper {
	public Usuario getUsuarioFromDto(UsuarioDto usuarioDto) {
		Usuario usuario = new Usuario();		
		usuario.setLogin(usuarioDto.getLogin());
		usuario.setCpf(usuarioDto.getCpf());
		usuario.setNome(usuarioDto.getNome());
		usuario.setSenha(usuarioDto.getSenha());
		return usuario;
	}
	
	public UsuarioDto getUsuarioDtoFromEntity(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setLogin(usuario.getLogin());
		usuarioDto.setCpf(usuario.getCpf());
		usuarioDto.setNome(usuario.getNome());
		usuarioDto.setSenha(usuario.getSenha());
		return usuarioDto;
	}
}
