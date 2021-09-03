package com.sysmei.controller;

import com.sysmei.dto.AgendaDTO;
import com.sysmei.dto.AgendaSomaDTO;
import com.sysmei.keys.HeaderKeys;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.keys.SysmeiKeys;
import com.sysmei.model.Agenda;
import com.sysmei.service.impl.AgendaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping(value = RotasKeys.AGENDA) public class AgendaController {

    @Autowired private AgendaServiceImpl agendaService;

    /**
     * @param login Login do usuário, podendo ser o email.
     * @return Retorna uma lista de AgendaDTO
     */

    @GetMapping public ResponseEntity<List<AgendaDTO>> findAll(
        @RequestParam(name = ParamsKeys.LOGIN) String login) {
        List<Agenda> list = agendaService.getAgendasWithLogin(login);
        List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * @param login            Enviar o Login do usuário, podendo ser o email
     * @param stringDataInicio Enviar a Data de inicio de pesquisa
     * @param stringDataFim    Enviar a data fim de pesquisa
     * @return Retorna uma lista de AgendaDTO
     */

    @GetMapping(RotasKeys.PUBLIC) public ResponseEntity<List<AgendaDTO>> findAllPublic(
        @RequestParam(name = ParamsKeys.LOGIN) String login,
        @RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
        @RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim) {
        String inicioDate = stringDataInicio;
        LocalDate dataInicio =
            LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));
        String fimDate = stringDataFim;
        LocalDate dataFim =
            LocalDate.parse(fimDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));

        List<Agenda> list = agendaService.getAgendasWithDateBetween(login, dataInicio, dataFim);
        List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * @return Retorna uma lista de AgendaDTO
     * @path id Enviar o ID do Paciente
     */

    @GetMapping(RotasKeys.ID) public ResponseEntity<List<AgendaDTO>> findAll(
        @PathVariable Long id) {
        List<Agenda> list = agendaService.getAgendasWithPaciente(id);
        List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * @param login            Enviar o Login do usuário, podendo ser o email
     * @param stringDataInicio Enviar a Data de inicio de pesquisa
     * @param stringDataFim    Enviar a data fim de pesquisa
     * @return Retorna o Objeto com a soma baseado no valor de cada agendamento
     */

    @GetMapping(RotasKeys.SOMA) public ResponseEntity<AgendaSomaDTO> getSomaAgendamentos(
        @RequestParam(name = ParamsKeys.LOGIN) String login,
        @RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
        @RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim) {
        String inicioDate = stringDataInicio;
        LocalDate dataInicio =
            LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));
        String fimDate = stringDataFim;
        LocalDate dataFim =
            LocalDate.parse(fimDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));

        AgendaSomaDTO agendaSoma =
            agendaService.getSomaAgendamentosBetween(login, dataInicio, dataFim);

        return ResponseEntity.ok().body(agendaSoma);
    }

    /**
     * @param status Enviar 0 para Não confirmado, 1 para confirmado 1x e 2 para confirmado 24h antes
     * @return Retorna uma lista de AgendaDTO
     * @Header login Enviar o Login do usuário, podendo ser o Email
     */

    @GetMapping(RotasKeys.STATUS) public ResponseEntity<List<AgendaDTO>> findAllStatus(
        @PathVariable Integer tipo,
        @RequestHeader(value = ParamsKeys.LOGIN, required = false) String login) {
        List<Agenda> list;
        if (login == null)
            list = agendaService.getAgendasWithStatus(tipo);
        else
            list = agendaService.getAgendasWithLoginAndStatus(login, tipo);
        List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * @param login            Enviar o Login do usuário, podendo ser o Email
     * @param stringDataInicio Enviar a Data de inicio de pesquisa
     * @param stringDataFim    Enviar a data fim de pesquisa
     * @param prestadorId      Enviar o ID do prestador
     * @return Retorna uma lista de AgendaDTO
     */

    @GetMapping(RotasKeys.PRESTADOR)
    public ResponseEntity<List<AgendaDTO>> findAgendasWithPrestador(
        @RequestParam(name = ParamsKeys.LOGIN) String login,
        @RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
        @RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim,
        @RequestParam(name = ParamsKeys.PRESTADOR_ID) Long prestadorId) {
        String inicioDate = stringDataInicio;
        LocalDate dataInicio =
            LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));
        String fimDate = stringDataFim;
        LocalDate dataFim =
            LocalDate.parse(fimDate, DateTimeFormatter.ofPattern(SysmeiKeys.DATE_FORMAT));

        List<Agenda> list =
            agendaService.getAgendasWithDateBetweenWithPrestador(login, dataInicio, dataFim,
                prestadorId);
        List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * @param dto Enviar o corpo da requisição o AgendaDTO formatado
     * @return Retorna o corpo montado com o status 201 CREATED
     */
    @PostMapping public ResponseEntity<Agenda> insert(@RequestBody AgendaDTO dto) {
        return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
    }

    /**
     * @param objDto
     * @param id
     * @return
     */

    @PutMapping(RotasKeys.ID) public ResponseEntity<Void> update(@RequestBody AgendaDTO objDto,
        @PathVariable Long id, @RequestHeader(value = HeaderKeys.PACIENTE_ID) Long pacienteId,
        @RequestHeader(value = HeaderKeys.PRESTADOR_ID) Long prestadorId) {
        objDto.setPaciente_id(pacienteId);
        objDto.setPrestador_id(prestadorId);
        Agenda obj = agendaService.fromDTO(objDto);
        obj.setId(id);
        obj = agendaService.update(obj);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param objDto
     * @param id
     * @return
     */

    @PatchMapping(RotasKeys.ID) public ResponseEntity<Void> updatePartes(
        @RequestBody AgendaDTO objDto, @PathVariable Long id,
        @RequestHeader(value = HeaderKeys.PACIENTE_ID) Long pacienteId,
        @RequestHeader(value = HeaderKeys.PRESTADOR_ID) Long prestadorId) {
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

    @DeleteMapping(RotasKeys.ID) public ResponseEntity<Void> delete(@PathVariable Long id) {
        agendaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
