package com.dentalclinic.controller;

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

import com.dentalclinic.dto.AgendaDTO;
import com.dentalclinic.model.Agenda;
import com.dentalclinic.service.AgendaService;

@RestController
@RequestMapping(value = "/agenda")
public class AgendaController {

	@Autowired
	private AgendaService agendaService;

	@RequestMapping(method = RequestMethod.GET, params = { "login" })
	public ResponseEntity<List<AgendaDTO>> findAll(@RequestParam(name = "login") String login) {
		List<Agenda> list = agendaService.getAgendasWithLogin(login);
		List<AgendaDTO> listDto = list.stream().map(obj -> new AgendaDTO(obj)).collect(Collectors.toList());  
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "id" })
	public ResponseEntity<List<AgendaDTO>> findAll(@RequestParam(name = "id") Long id) {
		List<Agenda> list = agendaService.getAgendasWithPaciente(id);
		List<AgendaDTO> listDto = list.stream().map(obj -> new AgendaDTO(obj)).collect(Collectors.toList());  
		return ResponseEntity.ok().body(listDto);
	}


	@PostMapping
	public ResponseEntity<Agenda> insert(@RequestBody AgendaDTO dto) {
		return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody AgendaDTO objDto, @PathVariable Long id) {
		Agenda obj = agendaService.fromDTO(objDto);
		obj.setId(id);
		obj = agendaService.update(obj);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		agendaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
