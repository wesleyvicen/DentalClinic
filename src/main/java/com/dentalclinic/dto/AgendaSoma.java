package com.dentalclinic.dto;

import java.time.LocalDate;

public class AgendaSoma {
	private String login;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private Double soma;

	public AgendaSoma(String login, LocalDate dataInicio, LocalDate dataFim, Double soma) {
		super();
		this.login = login;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.soma = soma;
	}

	public AgendaSoma() {
		super();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public Double getSoma() {
		return soma;
	}

	public void setSoma(Double soma) {
		this.soma = soma;
	}

}
