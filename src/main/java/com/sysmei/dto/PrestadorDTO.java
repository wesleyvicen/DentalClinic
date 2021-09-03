/**
 *
 */
package com.sysmei.dto;

import com.sysmei.model.Prestador;

public class PrestadorDTO {
    private String nome;
    private String login_usuario;
    private String telefone;

    public PrestadorDTO(String nome, String login_usuario, String telefone) {
        this.nome = nome;
        this.login_usuario = login_usuario;
    }

    public PrestadorDTO(Prestador entity) {
        this.nome = entity.getNome();
        this.login_usuario = entity.getUsuario().getLogin();
        this.telefone = entity.getTelefone();
    }

    public PrestadorDTO() {
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogin_usuario() {
        return login_usuario;
    }

    public void setLogin_usuario(String login_usuario) {
        this.login_usuario = login_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
