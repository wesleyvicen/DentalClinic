package com.dentalclinic.repository;


import java.time.LocalDate;
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
	
	//@Query(value = "Select agenda from Agenda agenda where agenda.allDay between :dataInicio and :dataFim a")
	@Query(value = "Select agenda from Agenda agenda where agenda.usuario.login = :loginUsuario and agenda.allDay between :dataInicio and :dataFim")
	public List<Agenda> getAgendasWithDateBetween(@Param("loginUsuario") String loginUsuario,@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

	
	@Query(value = "Select sum(agenda.valor) from Agenda agenda where agenda.usuario.login = :loginUsuario and agenda.allDay between :dataInicio and :dataFim")
	public Double getSomaAgendamentosBetween(@Param("loginUsuario") String loginUsuario, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
//	@Query(value = "Select agenda from Agenda agenda where agenda.allDay between :dataInicio and :dataFim a")
//	public List<Agenda> getAgendasWithDateBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

	
//	@Query(value = "select allDay, sum(valor) from Agenda where (allDay >= :allDayStart and data <= :allDayEnd) group by allDay")
//	String getSomaValores(LocalDate allDayStart, LocalDate allDayEnd);
	
}
