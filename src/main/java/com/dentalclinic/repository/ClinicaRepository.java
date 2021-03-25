package com.dentalclinic.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dentalclinic.model.Clinica;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {

	List<Clinica> findAll();
}
