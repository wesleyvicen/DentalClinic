package com.sysmei.repository;

import com.sysmei.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {

    List<Prestador> findAll();

    @Query(value = "Select prestador from Prestador prestador Left Join Fetch prestador.usuario usuario where prestador.usuario.login = :loginUsuario ORDER BY prestador.nome")
    List<Prestador> getPrestadorWithLogin(@Param("loginUsuario") String login);

    @Query(value = "Select prestador from Prestador prestador where prestador.telefone = :telefone")
    Optional<Prestador> findByTelefone(@PathVariable("telefone") String telefone);
}
