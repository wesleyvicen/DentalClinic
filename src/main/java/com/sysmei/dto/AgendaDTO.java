package com.sysmei.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sysmei.model.Agenda;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgendaDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long id;
  private String title;
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime start;
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime end;
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate allDay;
  private Double valor;
  private Integer status;
  private Long paciente_id;
  private String login_usuario;
  private String pagamento;
  private String detalhes;
  private Long prestador_id;

  public AgendaDTO() {

  }

  public AgendaDTO(Agenda obj) {
    this.id = obj.getId();
    this.title = obj.getTitle();
    this.start = obj.getStart();
    this.end = obj.getEnd();
    this.allDay = obj.getAllDay();
    this.valor = obj.getValor();
    this.status = obj.getStatus();
    this.login_usuario = obj.getUsuario().getLogin();
    this.paciente_id = obj.getPaciente().getId();
    this.setPagamento(obj.getPagamento());
    this.setDetalhes(obj.getDetalhes());
    this.prestador_id = obj.getPrestador().getId() == null ? 1 : obj.getPrestador().getId();

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  public LocalDate getAllDay() {
    return allDay;
  }

  public void setAllDay(LocalDate allDay) {
    this.allDay = allDay;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getPaciente_id() {
    return paciente_id;
  }

  public void setPaciente_id(Long paciente_id) {
    this.paciente_id = paciente_id;
  }

  public String getLogin_usuario() {
    return login_usuario;
  }

  public void setLogin_usuario(String login_usuario) {
    this.login_usuario = login_usuario;
  }

  public Double getValor() {
    return valor;
  }

  public void setValor(Double valor) {
    this.valor = valor;
  }

  public String getPagamento() {
    return pagamento;
  }

  public void setPagamento(String pagamento) {
    this.pagamento = pagamento;
  }

  public String getDetalhes() {
    return detalhes;
  }

  public void setDetalhes(String detalhes) {
    this.detalhes = detalhes;
  }

  public Long getPrestador_id() {
    return prestador_id;
  }

  public void setPrestador_id(Long prestador_id) {
    this.prestador_id = prestador_id;
  }

}
