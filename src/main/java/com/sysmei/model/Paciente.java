package com.sysmei.model;

import com.sysmei.enums.TipoCivil;
import com.sysmei.enums.TipoPlano;
import com.sysmei.enums.TipoSexo;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Paciente")
public class Paciente implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  @Column(nullable = false)
  private String nome;
  private String socialName;
  private String email;
  private String telefone1;
  private String telefone2;
  private String telefone3;

  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
  private List<DocumentUrl> documentsUrl = new ArrayList<>();

  @ManyToOne()
  @JoinColumn(name = "login_usuario", referencedColumnName = "login")
  private Usuario usuario;

  private LocalDate nascimento;
  // Pessoa Responsavel, Apenas nome.
  private String responsavel;
  @Enumerated(EnumType.STRING)
  private TipoSexo sexo;
  @Enumerated(EnumType.STRING)
  private TipoCivil estadoCivil;
  private String indicacao;

  @Enumerated(EnumType.STRING)
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

  public Paciente() {}

  public Paciente(Long id, String nome, String socialName, String email, String telefone1,
      String telefone2, String telefone3, LocalDate nascimento, String responsavel, TipoSexo sexo,
      TipoCivil estadoCivil, String indicacao, TipoPlano planoSaude, String convenio, String rg,
      String cpf, String ocupacao, String endereco, String enderecoNum, String bairro,
      String cidade, String estado, String cep) {
    super();
    this.id = id;
    this.nome = nome;
    this.setSocialName(socialName);
    this.email = email;
    this.telefone1 = telefone1;
    this.telefone2 = telefone2;
    this.telefone3 = telefone3;
    this.nascimento = nascimento;
    this.responsavel = responsavel;
    this.sexo = sexo;
    this.estadoCivil = estadoCivil;
    this.indicacao = indicacao;
    this.planoSaude = planoSaude;
    this.convenio = convenio;
    this.rg = rg;
    this.cpf = cpf;
    this.ocupacao = ocupacao;
    this.endereco = endereco;
    this.enderecoNum = enderecoNum;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
  }

  public Paciente(String nome, String socialName, String email, String telefone1, String telefone2,
      String telefone3, LocalDate nascimento, String responsavel, TipoSexo sexo,
      TipoCivil estadoCivil, String indicacao, TipoPlano planoSaude, String convenio, String rg,
      String cpf, String ocupacao, String endereco, String enderecoNum, String bairro,
      String cidade, String estado, String cep) {
    super();
    this.nome = nome;
    this.setSocialName(socialName);
    this.email = email;
    this.telefone1 = telefone1;
    this.telefone2 = telefone2;
    this.telefone3 = telefone3;
    this.nascimento = nascimento;
    this.responsavel = responsavel;
    this.sexo = sexo;
    this.estadoCivil = estadoCivil;
    this.indicacao = indicacao;
    this.planoSaude = planoSaude;
    this.convenio = convenio;
    this.rg = rg;
    this.cpf = cpf;
    this.ocupacao = ocupacao;
    this.endereco = endereco;
    this.enderecoNum = enderecoNum;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone1() {
    return telefone1;
  }

  public void setTelefone1(String telefone1) {
    this.telefone1 = telefone1;
  }

  public String getTelefone2() {
    return telefone2;
  }

  public void setTelefone2(String telefone2) {
    this.telefone2 = telefone2;
  }

  public String getTelefone3() {
    return telefone3;
  }

  public void setTelefone3(String telefone3) {
    this.telefone3 = telefone3;
  }

  public List<DocumentUrl> getDocumentsUrl() {
    return documentsUrl;
  }

  public void setDocumentsUrl(List<DocumentUrl> documentsUrl) {
    this.documentsUrl = documentsUrl;
  }

  public LocalDate getNascimento() {
    return nascimento;
  }

  public void setNascimento(LocalDate nascimento) {
    this.nascimento = nascimento;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public TipoSexo getSexo() {
    return sexo;
  }

  public void setSexo(TipoSexo sexo) {
    this.sexo = sexo;
  }

  public TipoCivil getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(TipoCivil estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getIndicacao() {
    return indicacao;
  }

  public void setIndicacao(String indicacao) {
    this.indicacao = indicacao;
  }

  public TipoPlano getPlanoSaude() {
    return planoSaude;
  }

  public void setPlanoSaude(TipoPlano planoSaude) {
    this.planoSaude = planoSaude;
  }

  public String getConvenio() {
    return convenio;
  }

  public void setConvenio(String convenio) {
    this.convenio = convenio;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getOcupacao() {
    return ocupacao;
  }

  public void setOcupacao(String ocupacao) {
    this.ocupacao = ocupacao;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getEnderecoNum() {
    return enderecoNum;
  }

  public void setEnderecoNum(String enderecoNum) {
    this.enderecoNum = enderecoNum;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Paciente other = (Paciente) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public String getSocialName() {
    return socialName;
  }

  public void setSocialName(String socialName) {
    this.socialName = socialName;
  }

}
