package com.sysmei.enums;

public enum TipoPlano {
	P("Particular"),
	C("Convênio");

	private String descricao;

	private TipoPlano(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
