package com.sysmei.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessaoDto {
  public String dataInicio;
  public String dataFim;
  public String token;
  public UsuarioDto usuario;

}
