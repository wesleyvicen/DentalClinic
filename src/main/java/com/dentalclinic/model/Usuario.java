/**
 * 
 */
package com.dentalclinic.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.dentalclinic.dto.UsuarioDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.Perfil;

/**
 * @author B�rbara Rodrigues, Gabriel Botelho, Guilherme Cruz, Lucas Caputo,
 *         Renan Alencar, Wesley Vicente
 *
 */
@Entity
public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false, length = 20)
	private String login;
	@JsonIgnore
	@Column(nullable = false, length = 255)
	private String senha;
	@Column(length = 50)
	private String nome;
	@Column(nullable = false, length = 11)
	private String cpf;
	/*
	 * @OneToMany(mappedBy = "usuario") private List<Conta> contas;
	 */

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();

	public Usuario() {

	}

	public Usuario(String login, String senha, String nome, String cpf) {
		super();
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.cpf = cpf;
	}

	public Usuario(UsuarioDto usuarioDto) {
		this.login = usuarioDto.getLogin();
		this.senha = usuarioDto.getSenha();
		this.nome = usuarioDto.getNome();
		this.cpf = usuarioDto.getCpf();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	@Override
	public String toString() {
		return "Usuario [login=" + login + ", senha=" + senha + ", nome=" + nome + ", cpf=" + cpf + "]";
	}

}
