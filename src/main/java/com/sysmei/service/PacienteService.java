package com.sysmei.service;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

public interface PacienteService {

  public List<Paciente> getPacientesWithLogin(String login);

  public Paciente getById(Long id);

  public Paciente update(Paciente obj);

  public Paciente insert(Paciente paciente);

  public Page<Paciente> findPage(Integer page, Integer linesPerPage, String orderBy,
      String direction);

  public void delete(Long id);

  public Paciente fromDTO(NewPacienteDTO dto);

  public URI uploadProfilePicture(Long id, MultipartFile multipartFile);

}
