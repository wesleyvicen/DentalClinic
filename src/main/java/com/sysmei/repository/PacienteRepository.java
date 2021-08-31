package com.sysmei.repository;


import com.sysmei.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	List<Paciente> findAll();

	@Query(value = "Select paciente from Paciente paciente Left Join Fetch paciente.usuario usuario where paciente.usuario.login = :loginUsuario ORDER BY paciente.nome")
	List<Paciente> getPacientesWithLogin(@Param("loginUsuario") String login);

}
