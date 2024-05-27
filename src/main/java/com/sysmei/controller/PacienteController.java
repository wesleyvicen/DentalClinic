package com.sysmei.controller;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.dto.PacienteDTO;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Paciente;
import com.sysmei.service.impl.DocumentsServiceImpl;
import com.sysmei.service.impl.PacienteServiceImpl;
import com.sysmei.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = RotasKeys.PACIENTE)
public class PacienteController {

  @Autowired
  private PacienteServiceImpl pacienteService;

  @Autowired
  private S3ServiceImpl s3Service;

  @Autowired
  private DocumentsServiceImpl documentsService;

  /**
   * @param login
   * @return
   */

  @GetMapping
  public ResponseEntity<List<PacienteDTO>> findAll(
      @RequestParam(name = ParamsKeys.LOGIN) String login) {
    List<Paciente> list = pacienteService.getPacientesWithLogin(login);
    List<PacienteDTO> listDto = list.stream().map(PacienteDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param id
   * @return
   */

  @GetMapping(RotasKeys.ID)
  public ResponseEntity<Paciente> getById(@PathVariable Long id) {
    Paciente dto = pacienteService.getById(id);
    return ResponseEntity.ok().body(dto);
  }
  
  @GetMapping("/{id}/login")
  public ResponseEntity<Paciente> getByIdAndLoginUsuario(@PathVariable Long id, @RequestHeader("login_usuario") String loginUsuario) {
    Paciente dto = pacienteService.getByIdAndLoginUsuario(id, loginUsuario);
    return ResponseEntity.ok().body(dto);
  }

  /**
   * @param page
   * @param linesPerPage
   * @param orderBy
   * @param direction
   * @return
   */

  @GetMapping(RotasKeys.PAGE)
  public ResponseEntity<Page<PacienteDTO>> findPage(
      @RequestParam(value = RotasKeys.PAGE, defaultValue = "0") Integer page,
      @RequestParam(value = ParamsKeys.LINES_PER_PAGE, defaultValue = "24") Integer linesPerPage,
      @RequestParam(value = ParamsKeys.ORDER_BY, defaultValue = "nome") String orderBy,
      @RequestParam(value = ParamsKeys.DIRECTION, defaultValue = "ASC") String direction) {
    Page<Paciente> list = pacienteService.findPage(page, linesPerPage, orderBy, direction);
    Page<PacienteDTO> listDto = list.map(PacienteDTO::new);
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param id
   * @param file
   * @return
   */

  @PostMapping(RotasKeys.PACIENTE_ID)
  public ResponseEntity<Void> uploadProfilePicture(@PathVariable Long id,
      @RequestParam(name = ParamsKeys.FILE) MultipartFile file) {
    URI uri = pacienteService.uploadProfilePicture(id, file);
    return ResponseEntity.created(uri).build();
  }

  /**
   * @param dto
   * @return
   */

  @PostMapping
  public ResponseEntity<?> insert(@RequestBody NewPacienteDTO dto) {
    Paciente paciente = pacienteService.fromDTO(dto);

    return new ResponseEntity<>(pacienteService.insert(paciente), HttpStatus.CREATED);
  }

  /**
   * @param objDto
   * @param id
   * @return
   */

  @PutMapping(RotasKeys.ID)
  public ResponseEntity<Void> update(@RequestBody NewPacienteDTO objDto, @PathVariable Long id) {
    Paciente obj = pacienteService.fromDTO(objDto);
    obj.setId(id);
    obj = pacienteService.update(obj);
    return ResponseEntity.noContent().build();

  }

  /**
   * @param id
   * @return
   */

  @DeleteMapping(RotasKeys.ID)
  public ResponseEntity<?> delete(@PathVariable Long id) {
    pacienteService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * @param keyName
   * @param fileId
   * @return
   */

  @DeleteMapping(RotasKeys.DELETE)
  public ResponseEntity<String> deleteFile(
      @RequestParam(value = ParamsKeys.FILE_NAME) String keyName,
      @RequestParam(value = ParamsKeys.FILE_ID) Long fileId) {
    s3Service.deleteFile(keyName);
    documentsService.delete(fileId);
    final String response = "[" + keyName + "] detelado com sucesso.";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
