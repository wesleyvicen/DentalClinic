package com.sysmei.mapper;

import com.sysmei.dto.UsuarioDto;
import com.sysmei.model.Usuario;

public class UsuarioMapper {
	public Usuario getUsuarioFromDto(UsuarioDto usuarioDto) {
		Usuario usuario = new Usuario();		
		usuario.setLogin(usuarioDto.getLogin());
		usuario.setTelefone(usuarioDto.getTelefone());
		usuario.setNome(usuarioDto.getNome());
		usuario.setSenha(usuarioDto.getSenha());
		return usuario;
	}
	
	public UsuarioDto getUsuarioDtoFromEntity(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setLogin(usuario.getLogin());
		usuarioDto.setTelefone(usuario.getTelefone());
		usuarioDto.setNome(usuario.getNome());
		usuarioDto.setSenha(usuario.getSenha());
		return usuarioDto;
	}
}
