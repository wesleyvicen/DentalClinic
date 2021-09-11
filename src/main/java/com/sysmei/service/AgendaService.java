package com.sysmei.service;

import com.sysmei.dto.AgendaDTO;
import com.sysmei.dto.AgendaSomaDTO;
import com.sysmei.model.Agenda;

import java.time.LocalDate;
import java.util.List;

public interface AgendaService {
  public List<Agenda> findAll();

  public Agenda find(Long id);

  public List<Agenda> getAgendasWithLogin(String login);

  public List<Agenda> getAgendasWithDateBetween(String login, LocalDate dataInicio,
      LocalDate dataFim);

  public List<Agenda> getAgendasWithDateBetweenWithPrestador(String login, LocalDate dataInicio,
      LocalDate dataFim, Long prestadorId);

  public List<Agenda> getAgendasWithPaciente(Long id);

  public Agenda update(Agenda obj);

  public Agenda insert(AgendaDTO obj);

  public void delete(Long id);

  public Agenda fromDTO(AgendaDTO objDto);

  public AgendaSomaDTO getSomaAgendamentosBetween(String login, LocalDate dataInicio,
      LocalDate dataFim);

  public List<Agenda> getAgendasWithStatus(Integer status);

  public List<Agenda> getAgendasWithLoginAndStatus(String login, Integer status);
}
