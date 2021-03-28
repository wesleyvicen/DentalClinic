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
	// Pessoa Responsavel, Apenas nome.
	private String responsavel;
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
	private String ocupacao;
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

	public Paciente(Integer id, String nome, String email, LocalDateTime nascimento, String responsavel, TipoSexo sexo,
			TipoCivil estadoCivil, String indicacao, TipoPlano planoSaude, String convenio, String rg, String cpf,
			String ocupacao, String endereco, String enderecoNum, String bairro, String cidade, String estado,
			String cep) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.nascimento = nascimento;
		this.responsavel = responsavel;
		this.sexo = sexo;
		this.estadoCivil = estadoCivil;
		this.indicacao = indicacao;
		this.planoSaude = planoSaude;
		this.convenio = convenio;
		this.rg = rg;
		this.cpf = cpf;
		this.ocupacao = ocupacao;
		this.endereco = endereco;
		this.enderecoNum = enderecoNum;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
	}

	public Paciente(String nome, String email, LocalDateTime nascimento,
			String responsavel, TipoSexo sexo, TipoCivil estadoCivil, String indicacao, TipoPlano planoSaude,
			String convenio, String rg, String cpf, String ocupacao, String endereco, String enderecoNum, String bairro,
			String cidade, String estado, String cep) {
		super();
		this.nome = nome;
		this.email = email;
		this.nascimento = nascimento;
		this.responsavel = responsavel;
		this.sexo = sexo;
		this.estadoCivil = estadoCivil;
		this.indicacao = indicacao;
		this.planoSaude = planoSaude;
		this.convenio = convenio;
		this.rg = rg;
		this.cpf = cpf;
		this.ocupacao = ocupacao;
		this.endereco = endereco;
		this.enderecoNum = enderecoNum;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
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

	public LocalDateTime getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDateTime nascimento) {
		this.nascimento = nascimento;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public TipoSexo getSexo() {
		return sexo;
	}

	public void setSexo(TipoSexo sexo) {
		this.sexo = sexo;
	}

	public TipoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(TipoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(String indicacao) {
		this.indicacao = indicacao;
	}

	public TipoPlano getPlanoSaude() {
		return planoSaude;
	}

	public void setPlanoSaude(TipoPlano planoSaude) {
		this.planoSaude = planoSaude;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(String ocupacao) {
		this.ocupacao = ocupacao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEnderecoNum() {
		return enderecoNum;
	}

	public void setEnderecoNum(String enderecoNum) {
		this.enderecoNum = enderecoNum;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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
