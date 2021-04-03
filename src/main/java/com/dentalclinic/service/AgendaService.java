package com.dentalclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.model.Agenda;
import com.dentalclinic.repository.AgendaRepository;

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
	
	@Transactional
	public List<Agenda> getAgendasWithLogin(String login) {
		List<Agenda> list = agendaRepository.getAgendasWithLogin(login);
		return list;
	}

	@Transactional
	public Agenda insert(Agenda obj) {
		Agenda agenda = new Agenda();
		agenda.setTitle(obj.getTitle());

		agenda.setStart(obj.getStart());
		agenda.setEnd(obj.getEnd());
		agenda.setAllDay(obj.getAllDay());
		agenda.setStatus(obj.getStatus());
		agenda.setPaciente(null);
		agenda.setUsuario(usuarioService.getUsuarioWithLogin(obj.getUsuario().getLogin()));	
		agenda.setPaciente(pacienteService.getById(obj.getPaciente().getId()));

		agenda = agendaRepository.save(agenda);
		return agenda;
	}

}
