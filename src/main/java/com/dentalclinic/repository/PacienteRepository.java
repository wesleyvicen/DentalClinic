package com.dentalclinic.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dentalclinic.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	List<Paciente> findAll();
	
	@Query(value = "Select paciente from Paciente paciente Left Join Fetch paciente.usuario usuario where paciente.usuario.login = :loginUsuario")	
	List<Paciente> getPacientesWithLogin(@Param("loginUsuario") String login);
	
}
