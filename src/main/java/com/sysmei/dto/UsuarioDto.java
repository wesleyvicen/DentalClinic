package com.sysmei.dto;

import com.sysmei.model.Usuario;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioDto {
  private String telefone;
  private String login;
  private String nome;
  private String senha;
  private Boolean status;
  private boolean enabled;

  public UsuarioDto(Usuario usuario) {
    this.telefone = usuario.getTelefone();
    this.login = usuario.getLogin();
    this.nome = usuario.getNome();
    this.senha = usuario.getSenha();
    this.status = usuario.getStatus();
    this.enabled = usuario.isEnabled();
  }

  public UsuarioDto(String telefone, String login, String nome, String senha) {
    super();
    this.telefone = telefone;
    this.login = login;
    this.nome = nome;
    this.senha = senha;
  }

  public UsuarioDto(String telefone, String login, String nome, String senha, Boolean status) {
    super();
    this.telefone = telefone;
    this.login = login;
    this.nome = nome;
    this.senha = senha;
    this.status = status;
  }

}
