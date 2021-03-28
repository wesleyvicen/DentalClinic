package com.dentalclinic;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dentalclinic.model.Clinica;
import com.dentalclinic.model.Paciente;
import com.dentalclinic.repository.ClinicaRepository;
import com.dentalclinic.repository.PacienteRepository;

import enums.TipoCivil;
import enums.TipoPlano;
import enums.TipoSexo;

@SpringBootApplication
public class DesafioJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioJavaApplication.class, args);
	}
	 @Bean
	    public CommandLineRunner mappingDemo(ClinicaRepository clinicaRepository,
	                                         PacienteRepository pacienteRepository) {
	        return args -> {

	            // create a new book
	            Clinica clinica = new Clinica(null, "DentalClinic");

	            // save the book
	            clinicaRepository.save(clinica);

	            // create and save new pages
//	            pacienteRepository.save(new Paciente("Wesley Vicente", "wesley@teste.com", LocalDateTime.now(), "grimauro", TipoSexo.M, TipoCivil.S, "NGM", TipoPlano.P, " ", "994455", "12345678911", "Dev", "Emiliano braga...", "237", "centro", "recife", "PE"));
//	            pacienteRepository.save(new Paciente(65, "Java 8 contents", "Java 8", clinica));
//	            pacienteRepository.save(new Paciente(95, "Concurrency contents", "Concurrency", clinica));
	        };
	    }
}
