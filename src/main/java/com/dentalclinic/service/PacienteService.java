package com.dentalclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.NewPacienteDTO;
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
	public List<Paciente> findAll() {
		return pacienteRepository.findAll();

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

	@Transactional
	public Paciente insert(Paciente paciente) {

		paciente = pacienteRepository.save(paciente);
		return paciente;
	}

	public Paciente fromDTO(NewPacienteDTO dto) {
		Paciente paciente = new Paciente(dto.getNome(), dto.getEmail(), dto.getNascimento(), dto.getResponsavel(),
				dto.getSexo(), dto.getEstadoCivil(), dto.getIndicacao(), dto.getPlanoSaude(), dto.getConvenio(),
				dto.getRg(), dto.getCpf(), dto.getOcupacao(), dto.getEndereco(), dto.getEnderecoNum(), dto.getBairro(),
				dto.getCidade(), dto.getEstado(), dto.getCep());
		paciente.getTelefones().add(dto.getTelefone1());
		if (dto.getTelefone2() != null) {
			paciente.getTelefones().add(dto.getTelefone2());
		}
		if (dto.getTelefone3() != null) {
			paciente.getTelefones().add(dto.getTelefone3());
		}
		return paciente;
	}

}
