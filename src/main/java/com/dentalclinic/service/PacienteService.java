package com.dentalclinic.service;

import java.util.List;
import java.util.Optional;

import com.dentalclinic.model.Agenda;
import com.dentalclinic.model.Telefone;
import com.dentalclinic.service.exception.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

	public Paciente getById(Long id) {
		Optional<Paciente> obj = pacienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + id + ", Tipo: " + Paciente.class.getName()));
	}

	public Paciente update(Paciente obj) {
		Paciente newObj = getById(obj.getId());
		updateData(newObj, obj);
		return pacienteRepository.save(newObj);
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

	public void delete(Long id) {
		pacienteRepository.findById(id);
		try {

			pacienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque existe entidades relacionadas");
		}
	}

	private void updateData(Paciente newObj, Paciente obj) {

		newObj.setNome(obj.getNome() == null ? newObj.getNome() : obj.getNome());
		newObj.setEmail(obj.getEmail() == null ? newObj.getEmail() : obj.getEmail());
		newObj.setNascimento(obj.getNascimento() == null ? newObj.getNascimento() : obj.getNascimento());
		newObj.setResponsavel(obj.getResponsavel() == null ? newObj.getResponsavel() : obj.getResponsavel());
		newObj.setSexo(obj.getSexo() == null ? newObj.getSexo() : obj.getSexo());
		newObj.setEstadoCivil(obj.getEstadoCivil() == null ? newObj.getEstadoCivil() : obj.getEstadoCivil());
		newObj.setIndicacao(obj.getIndicacao() == null ? newObj.getIndicacao() : obj.getIndicacao());
		newObj.setPlanoSaude(obj.getPlanoSaude() == null ? newObj.getPlanoSaude() : obj.getPlanoSaude());
		newObj.setConvenio(obj.getConvenio() == null ? newObj.getConvenio() : obj.getConvenio());
		newObj.setRg(obj.getRg() == null ? newObj.getRg() : obj.getRg());
		newObj.setCpf(obj.getCpf() == null ? newObj.getCpf() : obj.getCpf());
		newObj.setOcupacao(obj.getOcupacao() == null ? newObj.getOcupacao() : obj.getOcupacao());
		newObj.setEndereco(obj.getEndereco() == null ? newObj.getEndereco() : obj.getEndereco());
		newObj.setEnderecoNum(obj.getEnderecoNum() == null ? newObj.getEnderecoNum() : obj.getEnderecoNum());
		newObj.setBairro(obj.getBairro() == null ? newObj.getBairro() : obj.getBairro());
		newObj.setCidade(obj.getCidade() == null ? newObj.getCidade() : obj.getCidade());
		newObj.setEstado(obj.getEstado() == null ? newObj.getEstado() : obj.getEstado());
		newObj.setCep(obj.getCep() == null ? newObj.getCep() : obj.getCep());

	}

	@Transactional
	public Paciente fromDTO(NewPacienteDTO dto) {
		Paciente paciente = new Paciente(dto.getNome(), dto.getEmail(), dto.getNascimento(), dto.getResponsavel(),
				dto.getSexo(), dto.getEstadoCivil(), dto.getIndicacao(), dto.getPlanoSaude(), dto.getConvenio(),
				dto.getRg(), dto.getCpf(), dto.getOcupacao(), dto.getEndereco(), dto.getEnderecoNum(), dto.getBairro(),
				dto.getCidade(), dto.getEstado(), dto.getCep());
		paciente.setUsuario(usuarioService.getUsuarioWithLogin(dto.getLogin_usuario()));
		Telefone tel1 = new Telefone(dto.getTelefone1(), paciente);
		paciente.getTelefones().add(tel1);
		if (dto.getTelefone2() != null) {
			Telefone tel2 = new Telefone(dto.getTelefone1(), paciente);
			paciente.getTelefones().add(tel2);
		}
		if (dto.getTelefone3() != null) {
			Telefone tel3 = new Telefone(dto.getTelefone1(), paciente);
			paciente.getTelefones().add(tel3);
		}
		return paciente;
	}

}
