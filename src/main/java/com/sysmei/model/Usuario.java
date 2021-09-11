/**
 *
 */
package com.sysmei.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sysmei.dto.UsuarioDto;
import com.sysmei.enums.Perfil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(nullable = false, length = 82)
  private String login;
  @JsonIgnore
  @Column(nullable = false, length = 255)
  private String senha;
  @Column(length = 50)
  private String nome;
  @Column(nullable = false, length = 11)
  private String telefone;
  /*
   * @OneToMany(mappedBy = "usuario") private List<Conta> contas;
   */

  private Boolean status = false;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "PERFIS")
  private Set<Integer> perfis = new HashSet<>();

  private String imageUrl;

  @Column(name = "verification_code", length = 64)
  private String verificationCode;

  private boolean enabled;

  public Usuario() {

  }

  public Usuario(String login, String senha, String nome, String telefone) {
    super();
    this.login = login;
    this.senha = senha;
    this.nome = nome;
    this.telefone = telefone;
  }

  public Usuario(String login, String senha, String nome, String telefone, Boolean status) {
    super();
    this.login = login;
    this.senha = senha;
    this.nome = nome;
    this.telefone = telefone;
    this.status = status;
  }

  public Usuario(UsuarioDto usuarioDto) {
    this.login = usuarioDto.getLogin();
    this.senha = usuarioDto.getSenha();
    this.nome = usuarioDto.getNome();
    this.telefone = usuarioDto.getTelefone();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public Set<Perfil> getPerfis() {
    return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
  }

  public void addPerfil(Perfil perfil) {
    perfis.add(perfil.getCod());
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Usuario [login=" + login + ", senha=" + senha + ", nome=" + nome + ", telefone="
        + telefone + "]";
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }



}
