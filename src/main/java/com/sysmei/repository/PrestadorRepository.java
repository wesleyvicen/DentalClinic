package com.sysmei.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.sysmei.model.Prestador;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {

	List<Prestador> findAll();

	@Query(value = "Select prestador from Prestador prestador Left Join Fetch prestador.usuario usuario where prestador.usuario.login = :loginUsuario")
	List<Prestador> getPrestadorWithLogin(@Param("loginUsuario") String login);

	@Query(value = "Select prestador from Prestador prestador where prestador.telefone = :telefone")
	Optional<Prestador> findByTelefone(@PathVariable("telefone") String telefone);
}
