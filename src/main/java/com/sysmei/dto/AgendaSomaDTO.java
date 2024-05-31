package com.sysmei.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendaSomaDTO {
  private String login;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private Double soma;

}
