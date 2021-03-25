package com.dentalclinic.dto;

import com.dentalclinic.model.Usuario;

/**
 * @author B�rbara Rodrigues, Gabriel Botelho, Guilherme Cruz, Lucas Caputo, Renan Alencar, Wesley Vicente
 *
 */
public class UsuarioDto {
	private String cpf;
	private String login;
	private String nome;
	private String senha;
	
	public UsuarioDto(Usuario usuario) {
		this.cpf = usuario.getCpf();
		this.login = usuario.getLogin();
		this.nome = usuario.getNome();
		this.senha = usuario.getSenha();
	}
	
	public UsuarioDto(String cpf, String login, String nome, String senha) {
		super();
		this.cpf = cpf;
		this.login = login;
		this.nome = nome;
		this.senha = senha;
	}
	
	public UsuarioDto() {
		// TODO Auto-generated constructor stub
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	@Override
	public String toString() {
		return "UsuarioDto [cpf=" + cpf + ", login=" + login + ", nome=" + nome + ", senha=" + senha + "]";
	}
	
	
	
}
