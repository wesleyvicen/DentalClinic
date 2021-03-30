package com.dentalclinic.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping
	public ResponseEntity<List<PacienteDTO>> findAll() {
		List<Paciente> list = pacienteService.findAll();
		List<PacienteDTO> listDto = list.stream().map(obj -> new PacienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
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
}
