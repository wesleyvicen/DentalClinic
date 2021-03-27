package com.dentalclinic.dto;

import java.io.Serializable;

import com.dentalclinic.model.Clinica;

public class ClinicaDTO  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String nome;
	
	public ClinicaDTO() {
	}

	public ClinicaDTO(String nome) {
		this.nome = nome;
	}

	public ClinicaDTO(Clinica entity) {
		this.nome = entity.getNome();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
