/**
 * 
 */
package com.dentalclinic.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
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
	@Column(nullable = false, length = 82)
	private String login;
	@JsonIgnore
	@Column(nullable = false, length = 255)
	private String senha;
	@Column(length = 50)
	private String nome;
	@Column(nullable = false, length = 11)
	private String telefone;
	/*
	 * @OneToMany(mappedBy = "usuario") private List<Conta> contas;
	 */

	private Boolean status = false;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();

	public Usuario() {

	}

	public Usuario(String login, String senha, String nome, String telefone) {
		super();
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.telefone = telefone;
	}

	public Usuario(String login, String senha, String nome, String telefone, Boolean status) {
		super();
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.telefone = telefone;
		this.status = status;
	}

	public Usuario(UsuarioDto usuarioDto) {
		this.login = usuarioDto.getLogin();
		this.senha = usuarioDto.getSenha();
		this.nome = usuarioDto.getNome();
		this.telefone = usuarioDto.getTelefone();
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		StringBuilder builder = new StringBuilder();
		builder.append("Olá, ");
		builder.append(getNome()+"! \n");
		builder.append("\n");
		builder.append("Parabéns! você acabou de se registrar no nosso sistema. \n");
		builder.append("Use seu Email para entrar: ");
		builder.append(getLogin()+"\n");
		builder.append("Situação no Sistema: ");
		builder.append(status == true ? "Ativo": "Desativado");
		return builder.toString();
	}

}
