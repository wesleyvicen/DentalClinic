package com.sysmei.repository;


import com.sysmei.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findAll();

    @Query(value = "Select agenda from Agenda agenda Left Join Fetch agenda.usuario usuario where agenda.usuario.login = :loginUsuario")
    List<Agenda> getAgendasWithLogin(@Param("loginUsuario") String login);

    @Query(value = "Select agenda from Agenda agenda Left Join Fetch agenda.paciente paciente where agenda.paciente.id = :paciente_id")
    List<Agenda> getAgendasWithPaciente(@Param("paciente_id") Long pacienteId);

    @Query(value = "Select agenda from Agenda agenda where agenda.usuario.login = :loginUsuario and agenda.allDay between :dataInicio and :dataFim")
    public List<Agenda> getAgendasWithDateBetween(@Param("loginUsuario") String loginUsuario, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query(value = "Select sum(agenda.valor) from Agenda agenda where agenda.usuario.login = :loginUsuario and agenda.allDay between :dataInicio and :dataFim")
    public Double getSomaAgendamentosBetween(@Param("loginUsuario") String loginUsuario, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query(value = "Select agenda from Agenda agenda where agenda.status = :status")
    List<Agenda> getAgendasWithStatus(@Param("status") Integer status);

    @Query(value = "Select agenda from Agenda agenda Left Join Fetch agenda.usuario usuario where agenda.usuario.login = :loginUsuario and agenda.status =:status")
    List<Agenda> getAgendasWithLoginAndStatus(@Param("loginUsuario") String login, @Param("status") Integer status);

    @Query(value = "Select agenda from Agenda agenda where agenda.usuario.login = :loginUsuario and agenda.prestador.id = :prestadorId and agenda.allDay between :dataInicio and :dataFim")
    public List<Agenda> getAgendasWithDateBetweenWithPrestador(@Param("loginUsuario") String loginUsuario, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("prestadorId") Long prestadorId );

}
