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

import com.dentalclinic.dto.ClinicaDTO;
import com.dentalclinic.service.ClinicaService;

@RestController
@RequestMapping(value = "/clinica")
public class ClinicaController {

	@Autowired
	private ClinicaService clinicaService;

	@GetMapping
	public ResponseEntity<List<ClinicaDTO>> findAll() {
		List<ClinicaDTO> list = clinicaService.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping
	public ResponseEntity<ClinicaDTO> insert(@RequestBody ClinicaDTO dto) {
		return new ResponseEntity<>(clinicaService.insert(dto), HttpStatus.CREATED);
	}
}
