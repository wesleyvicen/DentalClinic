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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysmei.dto.AgendaDTO;
import com.sysmei.dto.AgendaSomaDTO;
import com.sysmei.keys.HeaderKeys;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Agenda;
import com.sysmei.service.impl.AgendaServiceImpl;

@RestController
@RequestMapping(value = RotasKeys.AGENDA)
public class AgendaController {

	@Autowired
	private AgendaServiceImpl agendaService;

	/**
	 * 
	 * @param login Login do usuário, podendo ser o email.
	 * @return Retorna uma lista de AgendaDTO
	 */

	@RequestMapping(method = RequestMethod.GET, params = { ParamsKeys.LOGIN })
	public ResponseEntity<List<AgendaDTO>> findAll(@RequestParam(name = ParamsKeys.LOGIN) String login) {
		List<Agenda> list = agendaService.getAgendasWithLogin(login);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param login            Enviar o Login do usuário, podendo ser o email
	 * @param stringDataInicio Enviar a Data de inicio de pesquisa
	 * @param stringDataFim    Enviar a data fim de pesquisa
	 * @return Retorna uma lista de AgendaDTO
	 */

	@RequestMapping(value = RotasKeys.PUBLIC, method = RequestMethod.GET, params = { ParamsKeys.LOGIN })
	public ResponseEntity<List<AgendaDTO>> findAllPublic(@RequestParam(name = ParamsKeys.LOGIN) String login,
			@RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
			@RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim) {
		String inicioDate = stringDataInicio;
		LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String fimDate = stringDataFim;
		LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		List<Agenda> list = agendaService.getAgendasWithDateBetween(login, dataInicio, dataFim);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param id Enviar o ID do Paciente
	 * @return Retorna uma lista de AgendaDTO
	 */

	@RequestMapping(method = RequestMethod.GET, params = { ParamsKeys.ID })
	public ResponseEntity<List<AgendaDTO>> findAll(@RequestParam(name = ParamsKeys.ID) Long id) {
		List<Agenda> list = agendaService.getAgendasWithPaciente(id);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param login            Enviar o Login do usuário, podendo ser o email
	 * @param stringDataInicio Enviar a Data de inicio de pesquisa
	 * @param stringDataFim    Enviar a data fim de pesquisa
	 * @return Retorna o Objeto com a soma baseado no valor de cada agendamento
	 */

	@RequestMapping(value = "/soma", method = RequestMethod.GET, params = { ParamsKeys.LOGIN })
	public ResponseEntity<AgendaSomaDTO> getSomaAgendamentos(@RequestParam(name = ParamsKeys.LOGIN) String login,
			@RequestParam(name = "dataInicio") String stringDataInicio,
			@RequestParam(name = "dataFim") String stringDataFim) {
		String inicioDate = stringDataInicio;
		LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String fimDate = stringDataFim;
		LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		AgendaSomaDTO agendaSoma = agendaService.getSomaAgendamentosBetween(login, dataInicio, dataFim);

		return ResponseEntity.ok().body(agendaSoma);
	}

	/**
	 * 
	 * @param status Enviar 0 para Não confirmado, 1 para confirmado 1x e 2 para
	 *               confirmado 24h antes
	 * @return Retorna uma lista de AgendaDTO
	 */

	@RequestMapping(method = RequestMethod.GET, params = { ParamsKeys.STATUS })
	public ResponseEntity<List<AgendaDTO>> findAllStatus(@RequestParam(name = ParamsKeys.STATUS) Integer status) {
		List<Agenda> list = agendaService.getAgendasWithStatus(status);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param login  Enviar o Login do usuário, podendo ser o Email
	 * @param status Enviar 0 para Não confirmado, 1 para confirmado 1x e 2 para
	 *               confirmado 24h antes
	 * @return Retorna uma lista de AgendaDTO
	 */

	@RequestMapping(method = RequestMethod.GET, params = { ParamsKeys.LOGIN, ParamsKeys.STATUS })
	public ResponseEntity<List<AgendaDTO>> findAllStatus(@RequestParam(name = ParamsKeys.LOGIN) String login,
			@RequestParam(name = ParamsKeys.STATUS) Integer status) {
		List<Agenda> list = agendaService.getAgendasWithLoginAndStatus(login, status);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param login            Enviar o Login do usuário, podendo ser o Email
	 * @param stringDataInicio Enviar a Data de inicio de pesquisa
	 * @param stringDataFim    Enviar a data fim de pesquisa
	 * @param prestadorId      Enviar o ID do prestador
	 * @return Retorna uma lista de AgendaDTO
	 */

	@RequestMapping(value = "/prestador", method = RequestMethod.GET, params = { ParamsKeys.LOGIN, "prestadorId" })
	public ResponseEntity<List<AgendaDTO>> findAgendasWithPrestador(@RequestParam(name = ParamsKeys.LOGIN) String login,
			@RequestParam(name = ParamsKeys.DATA_INICIO) String stringDataInicio,
			@RequestParam(name = ParamsKeys.DATA_FIM) String stringDataFim,
			@RequestParam(name = "prestadorId") Long prestadorId) {
		String inicioDate = stringDataInicio;
		LocalDate dataInicio = LocalDate.parse(inicioDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String fimDate = stringDataFim;
		LocalDate dataFim = LocalDate.parse(fimDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		List<Agenda> list = agendaService.getAgendasWithDateBetweenWithPrestador(login, dataInicio, dataFim,
				prestadorId);
		List<AgendaDTO> listDto = list.stream().map(AgendaDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param dto Enviar o corpo da requisição o AgendaDTO formatado
	 * @return Retorna o corpo montado com o status 201 CREATED
	 */
	@PostMapping
	public ResponseEntity<Agenda> insert(@RequestBody AgendaDTO dto) {
		return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param objDto
	 * @param id
	 * @return
	 */

	@RequestMapping(value = RotasKeys.ID, method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody AgendaDTO objDto, @PathVariable Long id,
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
	 * 
	 * @param objDto
	 * @param id
	 * @return
	 */

	@RequestMapping(value = RotasKeys.ID, method = RequestMethod.PATCH)
	public ResponseEntity<Void> updatePartes(@RequestBody AgendaDTO objDto, @PathVariable Long id,
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
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping(value = RotasKeys.ID, method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		agendaService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
