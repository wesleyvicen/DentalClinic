package com.sysmei.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysmei.dto.AgendaDTO;
import com.sysmei.dto.AgendaSoma;
import com.sysmei.keys.Keys;
import com.sysmei.model.Agenda;
import com.sysmei.service.AgendaService;

@RestController
@RequestMapping(value = Keys.AGENDA)
public class AgendaController {

	@Autowired
	private AgendaService agendaService;

	@RequestMapping(method = RequestMethod.GET, params = { Keys.PARAM_LOGIN })
	public ResponseEntity<List<AgendaDTO>> findAll(@RequestParam(name = Keys.PARAM_LOGIN) String login) {
		List<Agenda> list = agendaService.getAgendasWithLogin(login);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@RequestMapping(value = Keys.PUBLIC, method = RequestMethod.GET, params = { Keys.PARAM_LOGIN })
	public ResponseEntity<List<AgendaDTO>> findAllPublic(@RequestParam(name = Keys.PARAM_LOGIN) String login,
			@RequestParam(name = Keys.PARAM_DATA_INICIO) String stringDataInicio,
			@RequestParam(name = Keys.PARAM_DATA_FIM) String stringDataFim) {
		String inicioDate = stringDataInicio;
		LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String fimDate = stringDataFim;
		LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		List<Agenda> list = agendaService.getAgendasWithDateBetween(login, dataInicio, dataFim);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@RequestMapping(method = RequestMethod.GET, params = { Keys.ID })
	public ResponseEntity<List<AgendaDTO>> findAll(@RequestParam(name = Keys.ID) Long id) {
		List<Agenda> list = agendaService.getAgendasWithPaciente(id);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@PostMapping
	public ResponseEntity<Agenda> insert(@RequestBody AgendaDTO dto) {
		return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
	}

	@RequestMapping(value = Keys.ID, method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody AgendaDTO objDto, @PathVariable Long id) {
		Agenda obj = agendaService.fromDTO(objDto);
		obj.setId(id);
		obj = agendaService.update(obj);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = Keys.ID, method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		agendaService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/soma", method = RequestMethod.GET, params = { Keys.PARAM_LOGIN })
	public ResponseEntity<AgendaSoma> getSomaAgendamentos(@RequestParam(name = Keys.PARAM_LOGIN) String login,
			@RequestParam(name = "dataInicio") String stringDataInicio,
			@RequestParam(name = "dataFim") String stringDataFim) {
		String inicioDate = stringDataInicio;
		LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String fimDate = stringDataFim;
		LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		AgendaSoma agendaSoma = agendaService.getSomaAgendamentosBetween(login, dataInicio, dataFim);

		return ResponseEntity.ok().body(agendaSoma);
	}

	@RequestMapping(method = RequestMethod.GET, params = { Keys.PARAM_STATUS })
	public ResponseEntity<List<AgendaDTO>> findAllStatus(@RequestParam(name = Keys.PARAM_STATUS) Integer status) {
		List<Agenda> list = agendaService.getAgendasWithStatus(status);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@RequestMapping(method = RequestMethod.GET, params = { Keys.PARAM_LOGIN, Keys.PARAM_STATUS })
	public ResponseEntity<List<AgendaDTO>> findAllStatus(@RequestParam(name = Keys.PARAM_LOGIN) String login,
			@RequestParam(name = Keys.PARAM_STATUS) Integer status) {
		List<Agenda> list = agendaService.getAgendasWithLoginAndStatus(login, status);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(value = "/prestador", method = RequestMethod.GET, params = { Keys.PARAM_LOGIN, "prestadorId" })
	public ResponseEntity<List<AgendaDTO>> findAgendasWithPrestador(@RequestParam(name = Keys.PARAM_LOGIN) String login,
			@RequestParam(name = Keys.PARAM_DATA_INICIO) String stringDataInicio,
			@RequestParam(name = Keys.PARAM_DATA_FIM) String stringDataFim,
			@RequestParam(name = "prestadorId") Long prestadorId) {
		String inicioDate = stringDataInicio;
		LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String fimDate = stringDataFim;
		LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		List<Agenda> list = agendaService.getAgendasWithDateBetweenWithPrestador(login, dataInicio, dataFim, prestadorId);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
}
