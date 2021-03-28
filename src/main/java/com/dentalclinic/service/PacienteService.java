package com.dentalclinic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.PacienteDTO;
import com.dentalclinic.model.Paciente;
import com.dentalclinic.repository.ClinicaRepository;
import com.dentalclinic.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;
	private ClinicaRepository clinicaRepository;

	@Transactional(readOnly = true)
	public List<PacienteDTO> findAll() {
		List<Paciente> list = pacienteRepository.findAll();
		return list.stream().map(x -> new PacienteDTO(x)).collect(Collectors.toList());
	}

//	@Transactional
//	public PacienteDTO insert(PacienteDTO dto) {
//		Paciente paciente = new Paciente(null, dto.getNome(), dto.getEmail(), dto.getNascimento(), dto.getResponsavel(),
//				dto.getSexo(), dto.getEstadoCivil(), dto.getIndicacao(), dto.getPlanoSaude(), dto.getConvenio(),
//				dto.getRg(), dto.getCpf(), dto.getOcupacao(), dto.getEndereco(), dto.getEnderecoNum(), dto.getBairro(),
//				dto.getCidade(), dto.getEstado(), dto.getCep());
//			clinicaRepository.save(clinica.getPaciente());
//			order.getProducts().add(product);
//			
//			
//		order = repository.save(order);
//		return new OrderDTO(order);
//	}

}
