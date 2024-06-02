package com.sysmei.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.dto.PacienteDTO;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Paciente;
import com.sysmei.security.JWTUtil;
import com.sysmei.service.impl.DocumentsServiceImpl;
import com.sysmei.service.impl.PacienteServiceImpl;
import com.sysmei.service.impl.S3ServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = RotasKeys.PACIENTE)
@Tag(name = "Paciente Controller", description = "Gerenciamento de pacientes")
public class PacienteController {

  @Autowired
  private PacienteServiceImpl pacienteService;

  @Autowired
  private S3ServiceImpl s3Service;

  @Autowired
  private DocumentsServiceImpl documentsService;
  
  @Autowired
  private JWTUtil jwtUtil;

  /**
   * @param login Login do usuário
   * @return Retorna uma lista de PacienteDTOs associados ao login fornecido
   */
  @Operation(summary = "Retorna uma lista de pacientes associados a um login específico")
  @GetMapping
  public ResponseEntity<List<PacienteDTO>> findAll(
      @Parameter(description = "Login do usuário", required = true) @RequestParam(name = ParamsKeys.LOGIN) String login) {
    List<Paciente> list = pacienteService.getPacientesWithLogin(login);
    List<PacienteDTO> listDto = list.stream().map(PacienteDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param id ID do paciente
   * @param token Token JWT do usuário
   * @return Retorna o paciente com o ID especificado
   */
  @Operation(summary = "Retorna um paciente pelo ID")
  @GetMapping(RotasKeys.ID)
  public ResponseEntity<Paciente> getById(@Parameter(description = "ID do paciente", required = true) @PathVariable Long id, 
                                          @Parameter(description = "Token JWT do usuário", required = true) @RequestHeader("Authorization") String token) {
    String loginUsuario = jwtUtil.getUsername(token.substring(7)); // Remove "Bearer " do token
    Paciente dto = pacienteService.getByIdAndLoginUsuario(id, loginUsuario);
    return ResponseEntity.ok().body(dto);
  }

  /**
   * @param page Número da página
   * @param linesPerPage Número de linhas por página
   * @param orderBy Campo para ordenação
   * @param direction Direção da ordenação (ASC ou DESC)
   * @return Retorna uma página de PacienteDTOs
   */
  @Operation(summary = "Retorna uma página de pacientes")
  @GetMapping(RotasKeys.PAGE)
  public ResponseEntity<Page<PacienteDTO>> findPage(
      @Parameter(description = "Número da página", required = true) @RequestParam(value = RotasKeys.PAGE, defaultValue = "0") Integer page,
      @Parameter(description = "Número de linhas por página", required = true) @RequestParam(value = ParamsKeys.LINES_PER_PAGE, defaultValue = "24") Integer linesPerPage,
      @Parameter(description = "Campo para ordenação", required = true) @RequestParam(value = ParamsKeys.ORDER_BY, defaultValue = "nome") String orderBy,
      @Parameter(description = "Direção da ordenação", required = true) @RequestParam(value = ParamsKeys.DIRECTION, defaultValue = "ASC") String direction) {
    Page<Paciente> list = pacienteService.findPage(page, linesPerPage, orderBy, direction);
    Page<PacienteDTO> listDto = list.map(PacienteDTO::new);
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param id ID do paciente
   * @param file Arquivo de imagem para upload
   * @return Retorna URI do recurso criado
   */
  @Operation(summary = "Faz upload da foto de perfil de um paciente")
  @PostMapping(RotasKeys.PACIENTE_ID)
  public ResponseEntity<Void> uploadProfilePicture(@Parameter(description = "ID do paciente", required = true) @PathVariable Long id,
                                                   @Parameter(description = "Arquivo de imagem para upload", required = true) @RequestParam(name = ParamsKeys.FILE) MultipartFile file) {
    URI uri = pacienteService.uploadProfilePicture(id, file);
    return ResponseEntity.created(uri).build();
  }

  /**
   * @param dto Dados do novo paciente
   * @return Retorna o novo paciente criado
   */
  @Operation(summary = "Cria um novo paciente")
  @PostMapping
  public ResponseEntity<?> insert(@Parameter(description = "Dados do novo paciente", required = true) @RequestBody NewPacienteDTO dto) {
    Paciente paciente = pacienteService.fromDTO(dto);
    return new ResponseEntity<>(pacienteService.insert(paciente), HttpStatus.CREATED);
  }

  /**
   * @param objDto Dados atualizados do paciente
   * @param id ID do paciente
   * @return Resposta vazia com status 204 (No Content)
   */
  @Operation(summary = "Atualiza os dados de um paciente")
  @PutMapping(RotasKeys.ID)
  public ResponseEntity<Void> update(@Parameter(description = "Dados atualizados do paciente", required = true) @RequestBody NewPacienteDTO objDto, 
                                     @Parameter(description = "ID do paciente", required = true) @PathVariable Long id) {
    Paciente obj = pacienteService.fromDTO(objDto);
    obj.setId(id);
    obj = pacienteService.update(obj);
    return ResponseEntity.noContent().build();
  }

  /**
   * @param id ID do paciente
   * @return Resposta vazia com status 204 (No Content)
   */
  @Operation(summary = "Exclui um paciente pelo ID")
  @DeleteMapping(RotasKeys.ID)
  public ResponseEntity<?> delete(@Parameter(description = "ID do paciente", required = true) @PathVariable Long id) {
    pacienteService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * @param keyName Nome do arquivo
   * @param fileId ID do arquivo
   * @return Mensagem de sucesso
   */
  @Operation(summary = "Exclui um arquivo do S3")
  @DeleteMapping(RotasKeys.DELETE)
  public ResponseEntity<String> deleteFile(
      @Parameter(description = "Nome do arquivo", required = true) @RequestParam(value = ParamsKeys.FILE_NAME) String keyName,
      @Parameter(description = "ID do arquivo", required = true) @RequestParam(value = ParamsKeys.FILE_ID) Long fileId) {
    s3Service.deleteFile(keyName);
    documentsService.delete(fileId);
    final String response = "[" + keyName + "] deletado com sucesso.";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
