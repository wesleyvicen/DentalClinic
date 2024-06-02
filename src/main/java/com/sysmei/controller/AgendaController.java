package com.sysmei.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sysmei.dto.AgendaDTO;
import com.sysmei.dto.AgendaSomaDTO;
import com.sysmei.dto.StatusDTO;
import com.sysmei.keys.HeaderKeys;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.keys.SysmeiKeys;
import com.sysmei.model.Agenda;
import com.sysmei.service.impl.AgendaServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = RotasKeys.AGENDA)
@Tag(name = "Agenda Controller", description = "Gerenciamento de agendamentos")
public class AgendaController {

  @Autowired
  private AgendaServiceImpl agendaService;

  /**
   * @param login Login do usuário, podendo ser o email.
   * @return Retorna uma lista de AgendaDTO
   */
  @Operation(summary = "Retorna uma lista de agendamentos do usuário")
  @GetMapping
  public ResponseEntity<List<AgendaDTO>> findAll(
      @Parameter(description = "Login do usuário, podendo ser o email", required = true) @RequestParam(name = ParamsKeys.LOGIN) String login) {
    List<Agenda> list = agendaService.getAgendasWithLogin(login);
    List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param login Enviar o Login do usuário, podendo ser o email
   * @param stringDataInicio Enviar a Data de inicio de pesquisa
   * @param stringDataFim Enviar a data fim de pesquisa
   * @return Retorna uma lista de AgendaDTO
   */
  @Operation(summary = "Retorna uma lista de agendamentos públicos do usuário")
  @GetMapping(RotasKeys.PUBLIC)
  public ResponseEntity<List<AgendaDTO>> findAllPublic(
      @Parameter(description = "Login do usuário, podendo ser o email", required = true) @RequestParam(name = ParamsKeys.LOGIN) String login,
      @Parameter(description = "Data de inicio de pesquisa no formato yyyy-MM-dd", required = true) @RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
      @Parameter(description = "Data fim de pesquisa no formato yyyy-MM-dd", required = true) @RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim) {
    String inicioDate = stringDataInicio;
    LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));
    String fimDate = stringDataFim;
    LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));

    List<Agenda> list = agendaService.getAgendasWithDateBetween(login, dataInicio, dataFim);
    List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @return Retorna uma lista de AgendaDTO
   * @path id Enviar o ID do Paciente
   */
  @Operation(summary = "Retorna uma lista de agendamentos de um paciente específico")
  @GetMapping(RotasKeys.ID)
  public ResponseEntity<List<AgendaDTO>> findAll(@Parameter(description = "ID do paciente", required = true) @PathVariable Long id) {
    List<Agenda> list = agendaService.getAgendasWithPaciente(id);
    List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param login Enviar o Login do usuário, podendo ser o email
   * @param stringDataInicio Enviar a Data de inicio de pesquisa
   * @param stringDataFim Enviar a data fim de pesquisa
   * @return Retorna o Objeto com a soma baseado no valor de cada agendamento
   */
  @Operation(summary = "Retorna a soma dos agendamentos do usuário em um intervalo de datas")
  @GetMapping(RotasKeys.SOMA)
  public ResponseEntity<AgendaSomaDTO> getSomaAgendamentos(
      @Parameter(description = "Login do usuário, podendo ser o email", required = true) @RequestParam(name = ParamsKeys.LOGIN) String login,
      @Parameter(description = "Data de inicio de pesquisa no formato yyyy-MM-dd", required = true) @RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
      @Parameter(description = "Data fim de pesquisa no formato yyyy-MM-dd", required = true) @RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim) {
    String inicioDate = stringDataInicio;
    LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));
    String fimDate = stringDataFim;
    LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));

    AgendaSomaDTO agendaSoma = agendaService.getSomaAgendamentosBetween(login, dataInicio, dataFim);
    return ResponseEntity.ok().body(agendaSoma);
  }

  /**
   * @param status Enviar 0 para Não confirmado, 1 para confirmado 1x e 2 para confirmado 24h antes
   * @return Retorna uma lista de AgendaDTO
   * @Header login Enviar o Login do usuário, podendo ser o Email
   */
  @Operation(summary = "Retorna uma lista de agendamentos filtrados por status")
  @GetMapping(RotasKeys.STATUS)
  public ResponseEntity<List<AgendaDTO>> findAllStatus(@Parameter(description = "Status do agendamento", required = true) @PathVariable Integer tipo,
      @Parameter(description = "Login do usuário, podendo ser o email") @RequestHeader(value = ParamsKeys.LOGIN, required = false) String login) {
    List<Agenda> list;
    if (login == null)
      list = agendaService.getAgendasWithStatus(tipo);
    else
      list = agendaService.getAgendasWithLoginAndStatus(login, tipo);
    List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param login Enviar o Login do usuário, podendo ser o Email
   * @param stringDataInicio Enviar a Data de inicio de pesquisa
   * @param stringDataFim Enviar a data fim de pesquisa
   * @param prestadorId Enviar o ID do prestador ou "all" para retornar a agenda de todos os prestadores
   * @return Retorna uma lista de AgendaDTO, incluindo os nomes do paciente e do prestador
   */
  @Operation(summary = "Retorna uma lista de agendamentos de um prestador específico ou de todos os prestadores")
  @GetMapping(RotasKeys.PRESTADOR)
  public ResponseEntity<List<AgendaDTO>> findAgendasWithPrestador(
      @Parameter(description = "Login do usuário, podendo ser o email", required = true) @RequestParam(name = ParamsKeys.LOGIN) String login,
      @Parameter(description = "Data de inicio de pesquisa no formato yyyy-MM-dd", required = true) @RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
      @Parameter(description = "Data fim de pesquisa no formato yyyy-MM-dd", required = true) @RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim,
      @Parameter(description = "ID do prestador ou 'all' para todos os prestadores", required = true) @RequestParam(name = ParamsKeys.PRESTADOR_ID) String prestadorId) {
    String inicioDate = stringDataInicio;
    LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));
    String fimDate = stringDataFim;
    LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));

    List<Agenda> list = agendaService.getAgendasWithDateBetweenWithPrestador(login, dataInicio, dataFim, prestadorId);
    List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param dto Enviar o corpo da requisição o AgendaDTO formatado
   * @return Retorna o corpo montado com o status 201 CREATED
   */
  @Operation(summary = "Cria um novo agendamento")
  @PostMapping
  public ResponseEntity<Agenda> insert(@Parameter(description = "Dados do agendamento", required = true) @RequestBody AgendaDTO dto) {
    return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
  }

  /**
   * @param objDto
   * @param id
   * @return
   */
  @Operation(summary = "Atualiza um agendamento existente")
  @PutMapping(RotasKeys.ID)
  public ResponseEntity<Void> update(@Parameter(description = "Dados atualizados do agendamento", required = true) @RequestBody AgendaDTO objDto, 
      @Parameter(description = "ID do agendamento", required = true) @PathVariable Long id,
      @Parameter(description = "ID do paciente", required = true) @RequestHeader(value = HeaderKeys.PACIENTE_ID) Long pacienteId,
      @Parameter(description = "ID do prestador", required = true) @RequestHeader(value = HeaderKeys.PRESTADOR_ID) Long prestadorId) {
    objDto.setPaciente_id(pacienteId);
    objDto.setPrestador_id(prestadorId);
    Agenda obj = agendaService.fromDTO(objDto);
    obj.setId(id);
    agendaService.update(obj);
    return ResponseEntity.noContent().build();
  }
  
  /**
   * Atualiza o status de uma agenda específica.
   *
   * @param id    ID da agenda
   * @param statusDTO Novo status da agenda
   * @return ResponseEntity com status 204 (No Content)
   */
  @Operation(summary = "Atualiza o status de um agendamento")
  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> updateStatus(@Parameter(description = "ID da agenda", required = true) @PathVariable Long id, 
      @Parameter(description = "Novo status da agenda", required = true) @RequestBody StatusDTO statusDTO) {
    agendaService.updateStatus(id, statusDTO.getStatus());
    return ResponseEntity.noContent().build();
  }

  /**
   * @param objDto
   * @param id
   * @return
   */
  @Operation(summary = "Atualiza parcialmente um agendamento existente")
  @PatchMapping(RotasKeys.ID)
  public ResponseEntity<Void> updatePartes(@Parameter(description = "Dados parciais do agendamento", required = true) @RequestBody AgendaDTO objDto, 
      @Parameter(description = "ID do agendamento", required = true) @PathVariable Long id,
      @Parameter(description = "ID do paciente", required = true) @RequestHeader(value = HeaderKeys.PACIENTE_ID) Long pacienteId,
      @Parameter(description = "ID do prestador", required = true) @RequestHeader(value = HeaderKeys.PRESTADOR_ID) Long prestadorId) {
    objDto.setPaciente_id(pacienteId);
    objDto.setPrestador_id(prestadorId);
    Agenda obj = agendaService.fromDTO(objDto);
    obj.setId(id);
    obj = agendaService.update(obj);
    return ResponseEntity.noContent().build();
  }

  /**
   * @return
   * @path id
   */
  @Operation(summary = "Deleta um agendamento existente")
  @DeleteMapping(RotasKeys.ID)
  public ResponseEntity<Void> delete(@Parameter(description = "ID do agendamento", required = true) @PathVariable Long id) {
    agendaService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
