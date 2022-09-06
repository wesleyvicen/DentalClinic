/**
 *
 */
package com.sysmei.dto;

import com.sysmei.model.Prestador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrestadorDTO {
  private Long id;
  private String nome;
  private String login_usuario;
  private String telefone;

  public PrestadorDTO(String nome, String login_usuario, String telefone) {
    this.nome = nome;
    this.login_usuario = login_usuario;
    this.telefone = telefone;
  }

  public PrestadorDTO(Prestador entity) {
    this.id = entity.getId();
    this.nome = entity.getNome();
    this.login_usuario = entity.getUsuario().getLogin();
    this.telefone = entity.getTelefone();
  }

}
