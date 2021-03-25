package com.dentalclinic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.PacienteDTO;
import com.dentalclinic.model.Paciente;
import com.dentalclinic.repository.PacienteRepository;

@Service
public class PacienteService {
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Transactional(readOnly = true)
	public List<PacienteDTO> findAll() {
		List<Paciente> list = pacienteRepository.findAll();
		return list.stream().map(x -> new PacienteDTO(x)).collect(Collectors.toList());
	}


}
