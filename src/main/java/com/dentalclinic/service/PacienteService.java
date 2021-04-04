package com.dentalclinic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.NewPacienteDTO;
import com.dentalclinic.exceptions.ObjectNotFoundException;
import com.dentalclinic.model.Paciente;
import com.dentalclinic.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
//	@Transactional(readOnly = true)
//	public List<Paciente> findAll() {
//		return pacienteRepository.findAll();
//	}
	
	@Transactional
	public List<Paciente> getPacientesWithLogin(String login) {
		List<Paciente> list = pacienteRepository.getPacientesWithLogin(login);
		return list;
	}

	@Transactional
	public Paciente insert(Paciente paciente) {
		paciente = pacienteRepository.save(paciente);
		return paciente;
	}
	
	@Transactional
	public Page<Paciente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return pacienteRepository.findAll(pageRequest);
	}

	@Transactional
	public Paciente fromDTO(NewPacienteDTO dto) {
		Paciente paciente = new Paciente(dto.getNome(), dto.getEmail(), dto.getNascimento(), dto.getResponsavel(),
				dto.getSexo(), dto.getEstadoCivil(), dto.getIndicacao(), dto.getPlanoSaude(), dto.getConvenio(),
				dto.getRg(), dto.getCpf(), dto.getOcupacao(), dto.getEndereco(), dto.getEnderecoNum(), dto.getBairro(),
				dto.getCidade(), dto.getEstado(), dto.getCep());
		paciente.setUsuario(usuarioService.getUsuarioWithLogin(dto.getLogin_usuario()));	
		paciente.getTelefones().add(dto.getTelefone1());
		if (dto.getTelefone2() != null) {
			paciente.getTelefones().add(dto.getTelefone2());
		}
		if (dto.getTelefone3() != null) {
			paciente.getTelefones().add(dto.getTelefone3());
		}
		return paciente;
	}
	
	public Paciente getById(Long id) {
		Optional<Paciente> obj = pacienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + id + ", Tipo: " + Paciente.class.getName()));
	}

}
