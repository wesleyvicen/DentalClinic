package com.dentalclinic.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dentalclinic.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

	List<Agenda> findAll();
//	List<Agenda> findByDateBetween(final LocalDateTime start, final LocalDateTime end);
	@Query(value = "Select agenda from Agenda agenda Left Join Fetch agenda.usuario usuario where agenda.usuario.login = :loginUsuario")	
	List<Agenda> getAgendasWithLogin(@Param("loginUsuario") String login);
	
	@Query(value = "Select agenda from Agenda agenda Left Join Fetch agenda.paciente paciente where agenda.paciente.id = :paciente_id")	
	List<Agenda> getAgendasWithPaciente(@Param("paciente_id") Long pacienteId);
}
