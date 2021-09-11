package com.sysmei.enums;

public enum TipoCivil {
  S("Solteiro"), C("Casado"), D("Divorciado"), V("Viúvo");

  private String descricao;

  private TipoCivil(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
