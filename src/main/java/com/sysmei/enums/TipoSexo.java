package com.sysmei.enums;

public enum TipoSexo {
	M("Particular"),
	F("Convênio");

	private String descricao;

	private TipoSexo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
