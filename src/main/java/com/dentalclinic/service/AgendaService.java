package com.dentalclinic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.AgendaDTO;
import com.dentalclinic.exceptions.ObjectNotFoundException;
import com.dentalclinic.model.Agenda;
import com.dentalclinic.repository.AgendaRepository;
import com.dentalclinic.service.exception.DataIntegrityException;

@Service
public class AgendaService {

	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private UsuarioService usuarioService;

	@Transactional(readOnly = true)
	public List<Agenda> findAll() {
		List<Agenda> list = agendaRepository.findAll();
		return list;
	}
	
	public Agenda find(Long id) {
		Optional<Agenda> obj = agendaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + id + ", Tipo: " + Agenda.class.getName()));
	}
	
	@Transactional
	public List<Agenda> getAgendasWithLogin(String login) {
		List<Agenda> list = agendaRepository.getAgendasWithLogin(login);
		return list;
	}
	
	public Agenda update(Agenda obj) {
		Agenda newObj = find(obj.getId());
		updateData(newObj, obj);
		return agendaRepository.save(newObj);
	}

	@Transactional
	public Agenda insert(AgendaDTO obj) {
		Agenda agenda = new Agenda();
		agenda.setTitle(obj.getTitle());

		agenda.setStart(obj.getStart());
		agenda.setEnd(obj.getEnd());
		agenda.setAllDay(obj.getAllDay());
		agenda.setStatus(obj.getStatus());
		agenda.setUsuario(usuarioService.getUsuarioWithLogin(obj.getLogin_usuario()));	
		agenda.setPaciente(pacienteService.getById(obj.getPaciente_id()));

		agenda = agendaRepository.save(agenda);
		return agenda;
	}
	
	public void delete(Long id) {
		agendaRepository.findById(id);
		try {

			agendaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque existe entidades relacionadas");
		}
	}
	
	private void updateData(Agenda newObj, Agenda obj) {

		newObj.setTitle(obj.getTitle() == null ? newObj.getTitle() : obj.getTitle());
		newObj.setStart(obj.getStart() == null ? newObj.getStart() : obj.getStart());
		newObj.setEnd(obj.getEnd() == null ? newObj.getEnd() : obj.getEnd());
		newObj.setAllDay(obj.getAllDay() == null ? newObj.getAllDay() : obj.getAllDay());
		newObj.setStatus(obj.getStatus() == null ? newObj.getStatus() : obj.getStatus());
	}
	
	public Agenda fromDTO(AgendaDTO objDto) {
		return new Agenda(objDto.getId(), objDto.getTitle(), objDto.getStart(), objDto.getEnd(), objDto.getAllDay(), objDto.getStatus());
	}


}
