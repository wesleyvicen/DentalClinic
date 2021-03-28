package com.dentalclinic.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentalclinic.dto.AgendaDTO;
import com.dentalclinic.model.Agenda;
import com.dentalclinic.repository.AgendaRepository;

@Service
public class AgendaService {

	@Autowired
	private AgendaRepository agendaRepository;

	@Transactional(readOnly = true)
	public List<AgendaDTO> findAll() {
		List<Agenda> list = agendaRepository.findAll();
		return list.stream().map(x -> new AgendaDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public AgendaDTO insert(AgendaDTO dto) {
		Agenda agenda = new Agenda();
		agenda.setTitle(dto.getTitle());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime start = LocalDateTime.parse(dto.getStart(), formatter);
		agenda.setStart(start);
		LocalDateTime end = LocalDateTime.parse(dto.getEnd(), formatter);
		agenda.setEnd(end);
		LocalDate allDay = LocalDate.parse(dto.getAllDay(), formato);
		agenda.setAllDay(allDay);
		agenda.setStatus(dto.getStatus());

		agenda = agendaRepository.save(agenda);
		return new AgendaDTO(agenda);
	}

}
