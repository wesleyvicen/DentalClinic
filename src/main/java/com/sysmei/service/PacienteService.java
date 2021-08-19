package com.sysmei.service;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.model.Paciente;

public interface PacienteService {

	public List<Paciente> getPacientesWithLogin(String login);

	public Paciente getById(Long id);

	public Paciente update(Paciente obj);

	public Paciente insert(Paciente paciente);

	public Page<Paciente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);

	public void delete(Long id);

	public Paciente fromDTO(NewPacienteDTO dto);

	public URI uploadProfilePicture(Long id, MultipartFile multipartFile);

}
