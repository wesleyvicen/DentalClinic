package com.dentalclinic.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dentalclinic.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

	List<Agenda> findAll();
}
