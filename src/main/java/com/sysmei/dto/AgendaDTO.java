package com.sysmei.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sysmei.model.Agenda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
