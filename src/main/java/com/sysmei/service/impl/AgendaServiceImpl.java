package com.sysmei.service.impl;

import com.sysmei.dto.AgendaDTO;
import com.sysmei.dto.AgendaSomaDTO;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.model.Agenda;
import com.sysmei.repository.AgendaRepository;
import com.sysmei.service.AgendaService;
import com.sysmei.service.exception.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaServiceImpl implements AgendaService {

  @Autowired
  private AgendaRepository agendaRepository;

  @Autowired
  @Lazy
  private PacienteServiceImpl pacienteService;

  @Autowired
  @Lazy
  private UsuarioServiceImpl usuarioService;

  @Autowired
  private PrestadorServiceImpl prestadorService;

  @Transactional(readOnly = true)
  public List<Agenda> findAll() {
    List<Agenda> list = agendaRepository.findAll();
    return list;
  }

  public Agenda find(Long id) {
    Optional<Agenda> obj = agendaRepository.findById(id);
    return obj.orElseThrow(() -> new ObjectNotFoundException(
        "Objeto Não encontrado! ID: " + id + ", Tipo: " + Agenda.class.getName()));
  }

  @Transactional
  public List<Agenda> getAgendasWithLogin(String login) {
    List<Agenda> list = agendaRepository.getAgendasWithLogin(login);
    return list;
  }

  @Transactional
  public List<Agenda> getAgendasWithDateBetween(String login, LocalDate dataInicio,
      LocalDate dataFim) {
    List<Agenda> list = agendaRepository.getAgendasWithDateBetween(login, dataInicio, dataFim);
    return list;
  }

  @Transactional
  public List<Agenda> getAgendasWithDateBetweenWithPrestador(String login, LocalDate dataInicio,
      LocalDate dataFim, String prestadorId) {
    List<Agenda> list = agendaRepository.getAgendasWithDateBetweenWithPrestador(login, dataInicio, dataFim, prestadorId);
    return list;
  }

  public List<Agenda> getAgendasWithPaciente(Long id) {
    List<Agenda> list = agendaRepository.getAgendasWithPaciente(id);
    return list;
  }

  public Agenda update(Agenda obj) {
    Agenda newObj = find(obj.getId());
    updateData(newObj, obj);
    return agendaRepository.save(newObj);
  }

  @Transactional
  public Agenda insert(AgendaDTO obj) {
    Agenda agenda = new Agenda();
    agenda.setTitle(obj.getTitle());

    agenda.setStart(obj.getStart());
    agenda.setEnd(obj.getEnd());
    agenda.setAllDay(obj.getAllDay());
    agenda.setValor(obj.getValor());
    agenda.setStatus(obj.getStatus());
    agenda.setPagamento(obj.getPagamento());
    agenda.setDetalhes(obj.getDetalhes());
    agenda.setUsuario(usuarioService.getUsuarioWithLogin(obj.getLogin_usuario()));
    agenda.setPaciente(pacienteService.getById(obj.getPaciente_id()));
    agenda.setPrestador(prestadorService.getById(obj.getPrestador_id()));
    agenda = agendaRepository.save(agenda);
    return agenda;
  }

  public void delete(Long id) {
    try {
      agendaRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
          "Não é possível excluir porque existe entidades relacionadas");
    }
  }

  private void updateData(Agenda newObj, Agenda obj) {
    newObj.setPaciente(obj.getPaciente() == null ? newObj.getPaciente() : obj.getPaciente());
    newObj.setTitle(obj.getTitle() == null ? newObj.getTitle() : obj.getTitle());
    newObj.setStart(obj.getStart() == null ? newObj.getStart() : obj.getStart());
    newObj.setEnd(obj.getEnd() == null ? newObj.getEnd() : obj.getEnd());
    newObj.setAllDay(obj.getAllDay() == null ? newObj.getAllDay() : obj.getAllDay());
    newObj.setValor(obj.getValor() == null ? newObj.getValor() : obj.getValor());
    newObj.setStatus(obj.getStatus() == null ? newObj.getStatus() : obj.getStatus());
    newObj.setPagamento(obj.getPagamento() == null ? newObj.getPagamento() : obj.getPagamento());
    newObj.setDetalhes(obj.getDetalhes() == null ? newObj.getDetalhes() : obj.getDetalhes());
  }

  public Agenda fromDTO(AgendaDTO objDto) {
    Agenda agenda = new Agenda(objDto.getId(), objDto.getTitle(), objDto.getStart(),
        objDto.getEnd(), objDto.getAllDay(), objDto.getValor(), objDto.getStatus(),
        objDto.getPagamento(), objDto.getDetalhes());
    agenda.setPaciente(pacienteService.getById(objDto.getPaciente_id()));
    agenda.setPrestador(prestadorService.getById(objDto.getPrestador_id()));
    return agenda;
  }

  public AgendaSomaDTO getSomaAgendamentosBetween(String login, LocalDate dataInicio,
      LocalDate dataFim) {
    Double soma = agendaRepository.getSomaAgendamentosBetween(login, dataInicio, dataFim);
    if (soma == null) {
      soma = 0.0;
    }
    AgendaSomaDTO agendaSoma = new AgendaSomaDTO(login, dataInicio, dataFim, soma);
    return agendaSoma;
  }

  @Transactional
  public List<Agenda> getAgendasWithStatus(Integer status) {
    List<Agenda> list = agendaRepository.getAgendasWithStatus(status);
    return list;
  }

  @Transactional
  public List<Agenda> getAgendasWithLoginAndStatus(String login, Integer status) {
    List<Agenda> list = agendaRepository.getAgendasWithLoginAndStatus(login, status);
    return list;
  }
  
  @Transactional
  public Agenda updateStatus(Long id, Integer status) {
    Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
        "Objeto não encontrado! ID: " + id + ", Tipo: " + Agenda.class.getName()));
    agenda.setStatus(status);
    return agendaRepository.save(agenda);
  }

}
