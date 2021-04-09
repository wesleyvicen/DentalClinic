package com.dentalclinic.controller;

import java.util.List;

import com.dentalclinic.dto.AgendaDTO;
import com.dentalclinic.model.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dentalclinic.dto.NewPacienteDTO;
import com.dentalclinic.dto.PacienteDTO;
import com.dentalclinic.model.Paciente;
import com.dentalclinic.service.PacienteService;

@RestController
@RequestMapping(value = "/paciente")
public class PacienteController {

	@Autowired
	private PacienteService pacienteService;

	@RequestMapping(method = RequestMethod.GET, params = { "login" })
	public ResponseEntity<List<Paciente>> findAll(@RequestParam(name = "login") String login) {
		List<Paciente> list = pacienteService.getPacientesWithLogin(login);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Paciente> getById(@PathVariable Long id) {
		Paciente dto = pacienteService.getById(id);
		return ResponseEntity.ok().body(dto);
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<PacienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Paciente> list = pacienteService.findPage(page, linesPerPage, orderBy, direction);
		Page<PacienteDTO> listDto = list.map(obj -> new PacienteDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody NewPacienteDTO dto) {
		Paciente paciente = pacienteService.fromDTO(dto);

		return new ResponseEntity<>(pacienteService.insert(paciente), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody NewPacienteDTO objDto, @PathVariable Long id) {
		Paciente obj = pacienteService.fromDTO(objDto);
		obj.setId(id);
		obj = pacienteService.update(obj);
		return ResponseEntity.noContent().build();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		pacienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
