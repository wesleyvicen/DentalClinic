package com.sysmei.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysmei.dto.PrestadorDTO;
import com.sysmei.keys.ParamsKeys;
import com.sysmei.keys.RotasKeys;
import com.sysmei.model.Prestador;
import com.sysmei.service.impl.PrestadorServiceImpl;

@RestController
@RequestMapping(value = RotasKeys.PRESTADOR)
public class PrestadorController {

	@Autowired
	private PrestadorServiceImpl prestadorService;

	/**
	 * 
	 * @param login
	 * @return
	 */

	@RequestMapping(method = RequestMethod.GET, params = { ParamsKeys.LOGIN })
	public ResponseEntity<List<PrestadorDTO>> findAll(@RequestParam(name = ParamsKeys.LOGIN) String login) {
		List<Prestador> list = prestadorService.getPrestadorWithLogin(login);
		List<PrestadorDTO> listDto = list.stream().map(PrestadorDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping(RotasKeys.ID)
	public ResponseEntity<Prestador> getById(@PathVariable Long id) {
		Prestador dto = prestadorService.getById(id);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * 
	 * @param telefone
	 * @return
	 */

	@GetMapping(RotasKeys.BUSCA_TELEFONE)
	public ResponseEntity<Prestador> getByTelefone(@PathVariable String telefone) {
		Prestador dto = prestadorService.getByTelefone(telefone);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody PrestadorDTO dto) {
		Prestador prestador = prestadorService.fromDTO(dto);

		return new ResponseEntity<>(prestadorService.insert(prestador), HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param objDto
	 * @param id
	 * @return
	 */

	@RequestMapping(value = RotasKeys.ID, method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody PrestadorDTO objDto, @PathVariable Long id) {
		Prestador obj = prestadorService.fromDTO(objDto);
		obj.setId(id);
		obj = prestadorService.update(obj);
		return ResponseEntity.noContent().build();

	}

	/**
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping(value = RotasKeys.ID, method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		prestadorService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
