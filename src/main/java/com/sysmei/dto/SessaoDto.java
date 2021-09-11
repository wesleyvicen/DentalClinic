package com.sysmei.dto;

public class SessaoDto {
  public String dataInicio;
  public String dataFim;
  public String token;
  public UsuarioDto usuario;

  public String getDataFim() {
    return dataFim;
  }

  public void setDataFim(String dataFim) {
    this.dataFim = dataFim;
  }

  public String getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(String dataInicio) {
    this.dataInicio = dataInicio;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UsuarioDto getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDto usuarioDto) {
    this.usuario = usuarioDto;
  }

  @Override
  public String toString() {
    return "SessaoDto [dataFim=" + dataFim + ", dataInicio=" + dataInicio + ", token=" + token
        + ", usuario=" + usuario + "]";
  }


}
