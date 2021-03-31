package com.dentalclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.model.Agenda;
import com.dentalclinic.repository.AgendaRepository;
import com.dentalclinic.repository.UsuarioRepository;

@Service
public class AgendaService {

	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;

	@Transactional(readOnly = true)
	public List<Agenda> findAll() {
		List<Agenda> list = agendaRepository.findAll();
		return list;
	}

//	@Transactional(readOnly = true)
//	public List<Agenda> findByDate(String start, String end) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		Agenda agenda = new Agenda();
//		LocalDateTime startF = LocalDateTime.parse(start, formatter);
//		agenda.setStart(startF);
//		LocalDateTime endF = LocalDateTime.parse(start, formatter);
//		agenda.setEnd(endF);
//		return agendaRepository.findByDateBetween(startF, endF);
//	}
	
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
		agenda.setUsuario(usuarioService.getUsuarioWithLogin(obj.getUsuario().getLogin()));		

		agenda = agendaRepository.save(agenda);
		return agenda;
	}

}
