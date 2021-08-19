package com.sysmei.dto;

import com.sysmei.model.Usuario;

public class UsuarioDto {
	private String telefone;
	private String login;
	private String nome;
	private String senha;
	private Boolean status;
    private boolean enabled;

	public UsuarioDto(Usuario usuario) {
		this.telefone = usuario.getTelefone();
		this.login = usuario.getLogin();
		this.nome = usuario.getNome();
		this.senha = usuario.getSenha();
		this.status = usuario.getStatus();
		this.enabled = usuario.isEnabled();
	}

	public UsuarioDto(String telefone, String login, String nome, String senha) {
		super();
		this.telefone = telefone;
		this.login = login;
		this.nome = nome;
		this.senha = senha;
	}

	public UsuarioDto(String telefone, String login, String nome, String senha, Boolean status) {
		super();
		this.telefone = telefone;
		this.login = login;
		this.nome = nome;
		this.senha = senha;
		this.status = status;
	}

	public UsuarioDto() {
		// TODO Auto-generated constructor stub
	}

	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UsuarioDto [telefone=" + telefone + ", login=" + login + ", nome=" + nome + ", senha=" + senha + "]";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



}
