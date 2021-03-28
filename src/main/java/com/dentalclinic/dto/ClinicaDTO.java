package com.dentalclinic.dto;

import java.io.Serializable;

import com.dentalclinic.model.Clinica;

public class ClinicaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nome;

	public ClinicaDTO() {
	}

	public ClinicaDTO(String nome) {
		this.nome = nome;
	}

	public ClinicaDTO(String nome, Long id) {
		this.nome = nome;
		this.id = id;
	}

	public ClinicaDTO(Clinica entity) {
		this.nome = entity.getNome();
		this.id = entity.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
