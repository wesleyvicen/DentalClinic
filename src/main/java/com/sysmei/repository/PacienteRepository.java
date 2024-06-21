package com.sysmei.repository;


import com.sysmei.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

  List<Paciente> findAll();

  @Query(
	value = "SELECT paciente FROM Paciente paciente JOIN FETCH paciente.usuario usuario WHERE usuario.login = :loginUsuario ORDER BY paciente.nome")
  List<Paciente> getPacientesWithLogin(@Param("loginUsuario") String login);

  @Query("SELECT p FROM Paciente p WHERE p.id = :id AND p.usuario.login = :loginUsuario")
  Optional<Paciente> findByIdAndLoginUsuario(Long id, String loginUsuario);
  
  Optional<Paciente> findByEmail(String email);
  
  Optional<Paciente> findByResetPasswordToken(String token);

}
