package com.dentalclinic.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.TipoCivil;
import enums.TipoPlano;
import enums.TipoSexo;

@Entity
@Table(name = "Paciente")
public class Paciente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer id;
	@Column(nullable = false)
	private String nome;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "clinica_id")
	private Clinica clinica;
	private String email;
	

	@ElementCollection
	@CollectionTable(name = "CONTATO")
	private Set<String> telefones = new HashSet<>();

	@Column(nullable = false)
	private LocalDateTime nascimento;
	//Pessoa Responsavel, Apenas nome.
	private String Responsavel;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoSexo sexo;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoCivil estadoCivil;
	private String indicacao;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPlano planoSaude;
	
	private String convenio;
	private String rg;
	@Column(nullable = false)
	private String cpf;
	private String Ocupacao;
	@Column(nullable = false)
	private String endereco;
	private String enderecoNum;
	private String bairro;
	@Column(nullable = false)
	private String cidade;
	@Column(nullable = false)
	private String estado;
	@Column(nullable = false)
	private String cep;
	public Paciente() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clinica == null) ? 0 : clinica.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Clinica getClinica() {
		return clinica;
	}

	public void setClinica(Clinica clinica) {
		this.clinica = clinica;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
		if (clinica == null) {
			if (other.clinica != null)
				return false;
		} else if (!clinica.equals(other.clinica))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
