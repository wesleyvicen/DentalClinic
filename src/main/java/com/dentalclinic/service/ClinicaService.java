package com.dentalclinic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.ClinicaDTO;
import com.dentalclinic.model.Clinica;
import com.dentalclinic.repository.ClinicaRepository;

@Service
public class ClinicaService {

	@Autowired
	private ClinicaRepository clinicaRepository;

	@Transactional(readOnly = true)
	public List<ClinicaDTO> findAll() {
		List<Clinica> list = clinicaRepository.findAll();
		return list.stream().map(x -> new ClinicaDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public ClinicaDTO insert(ClinicaDTO dto) {
		Clinica clinica = new Clinica(null, dto.getNome());
		clinica = clinicaRepository.save(clinica);
		return new ClinicaDTO(clinica);
	}

}
