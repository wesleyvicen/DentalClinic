package com.sysmei.dto;

import com.sysmei.enums.TipoCivil;
import com.sysmei.enums.TipoPlano;
import com.sysmei.enums.TipoSexo;
import com.sysmei.model.DocumentUrl;
import com.sysmei.model.Paciente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long id;
  private String nome;
  private String socialName;
  private String email;

  private String telefone1;

  private String telefone2;

  private String telefone3;

  private LocalDate nascimento;
  // Pessoa Responsavel, Apenas nome.
  private String responsavel;
  private TipoSexo sexo;
  private TipoCivil estadoCivil;
  private String indicacao;

  private TipoPlano planoSaude;

  private String convenio;
  private String rg;
  private String cpf;
  private String ocupacao;
  private String endereco;
  private String enderecoNum;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;
  private List<DocumentUrl> documentsUrl = new ArrayList<>();
  private String login_usuario;

  public PacienteDTO(Paciente entity) {
    this.setId(entity.getId());
    this.nome = entity.getNome();
    this.setSocialName(entity.getSocialName());
    this.email = entity.getEmail();
    this.nascimento = entity.getNascimento();
    this.responsavel = entity.getResponsavel();
    this.sexo = entity.getSexo();
    this.estadoCivil = entity.getEstadoCivil();
    this.indicacao = entity.getIndicacao();
    this.planoSaude = entity.getPlanoSaude();
    this.convenio = entity.getConvenio();
    this.rg = entity.getRg();
    this.cpf = entity.getCpf();
    this.ocupacao = entity.getOcupacao();
    this.endereco = entity.getEndereco();
    this.enderecoNum = entity.getEnderecoNum();
    this.bairro = entity.getBairro();
    this.cidade = entity.getCidade();
    this.estado = entity.getEstado();
    this.cep = entity.getCep();
    this.telefone1 = entity.getTelefone1();
    this.telefone2 = entity.getTelefone2();
    this.telefone3 = entity.getTelefone3();
    this.setDocumentsUrl(entity.getDocumentsUrl());
    this.login_usuario = entity.getUsuario().getLogin();
  }

}
