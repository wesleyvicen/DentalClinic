package com.sysmei.controller;

import com.sysmei.dto.PrestadorDTO;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Prestador;
import com.sysmei.service.impl.PrestadorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = RotasKeys.PRESTADOR)
@Tag(name = "Prestador Controller", description = "Gerenciamento de prestadores")
public class PrestadorController {

  @Autowired
  private PrestadorServiceImpl prestadorService;

  /**
   * @param login Login do usuário
   * @return Retorna uma lista de PrestadorDTOs associados ao login fornecido
   */
  @Operation(summary = "Retorna uma lista de prestadores associados a um login específico")
  @RequestMapping(method = RequestMethod.GET, params = {ParamsKeys.LOGIN})
  public ResponseEntity<List<PrestadorDTO>> findAll(
      @Parameter(description = "Login do usuário", required = true) @RequestParam(name = ParamsKeys.LOGIN) String login) {
    List<Prestador> list = prestadorService.getPrestadorWithLogin(login);
    List<PrestadorDTO> listDto = list.stream().map(PrestadorDTO::new).collect(Collectors.toList());
    return ResponseEntity.ok().body(listDto);
  }

  /**
   * @param id ID do prestador
   * @return Retorna o prestador com o ID especificado
   */
  @Operation(summary = "Retorna um prestador pelo ID")
  @GetMapping(RotasKeys.ID)
  public ResponseEntity<Prestador> getById(@Parameter(description = "ID do prestador", required = true) @PathVariable Long id) {
    Prestador dto = prestadorService.getById(id);
    return ResponseEntity.ok().body(dto);
  }

  /**
   * @param telefone Telefone do prestador
   * @return Retorna o prestador com o telefone especificado
   */
  @Operation(summary = "Retorna um prestador pelo telefone")
  @GetMapping(RotasKeys.BUSCA_TELEFONE)
  public ResponseEntity<Prestador> getByTelefone(@Parameter(description = "Telefone do prestador", required = true) @PathVariable String telefone) {
    Prestador dto = prestadorService.getByTelefone(telefone);
    return ResponseEntity.ok().body(dto);
  }

  /**
   * @param dto Dados do novo prestador
   * @return Retorna o novo prestador criado
   */
  @Operation(summary = "Cria um novo prestador")
  @PostMapping
  public ResponseEntity<?> insert(@Parameter(description = "Dados do novo prestador", required = true) @RequestBody PrestadorDTO dto) {
    Prestador prestador = prestadorService.fromDTO(dto);
    return new ResponseEntity<>(prestadorService.insert(prestador), HttpStatus.CREATED);
  }

  /**
   * @param objDto Dados atualizados do prestador
   * @param id ID do prestador
   * @return Resposta vazia com status 204 (No Content)
   */
  @Operation(summary = "Atualiza os dados de um prestador")
  @RequestMapping(value = RotasKeys.ID, method = RequestMethod.PUT)
  public ResponseEntity<Void> update(@Parameter(description = "Dados atualizados do prestador", required = true) @RequestBody PrestadorDTO objDto, 
                                     @Parameter(description = "ID do prestador", required = true) @PathVariable Long id) {
    Prestador obj = prestadorService.fromDTO(objDto);
    obj.setId(id);
    obj = prestadorService.update(obj);
    return ResponseEntity.noContent().build();
  }

  /**
   * @param id ID do prestador
   * @return Resposta vazia com status 204 (No Content)
   */
  @Operation(summary = "Exclui um prestador pelo ID")
  @RequestMapping(value = RotasKeys.ID, method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@Parameter(description = "ID do prestador", required = true) @PathVariable Long id) {
    prestadorService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
