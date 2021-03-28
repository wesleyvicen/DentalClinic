package com.dentalclinic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalclinic.dto.AgendaDTO;
import com.dentalclinic.service.AgendaService;

@RestController
@RequestMapping(value = "/agenda")
public class AgendaController {

	@Autowired
	private AgendaService agendaService;

	@GetMapping
	public ResponseEntity<List<AgendaDTO>> findAll() {
		List<AgendaDTO> list = agendaService.findAll();
		return ResponseEntity.ok().body(list);
	}

	@PostMapping
	public ResponseEntity<AgendaDTO> insert(@RequestBody AgendaDTO dto) {
		return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
	}
}
