package com.sysmei.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.sysmei.dto.LoginDto;
import com.sysmei.dto.SessaoDto;
import com.sysmei.dto.UsuarioDto;
import com.sysmei.model.Usuario;

public interface UsuarioService {

	public UsuarioDto addUsuario(UsuarioDto usuarioDto) throws UnsupportedEncodingException, MessagingException;

	public Usuario getUsuarioWithLoginAndSenha(String login, String senha);

	public Usuario getUsuarioWithLogin(String login);

	public boolean existsUsuarioWithLogin(String login);

	public Usuario search(Integer id);

	public List<UsuarioDto> findAll(Integer id);

	public SessaoDto logar(LoginDto loginDto);

	public URI uploadProfilePicture(MultipartFile multipartFile);

	public boolean verificarUser(String verificationCode);

}
