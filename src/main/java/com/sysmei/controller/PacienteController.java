package com.sysmei.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.multipart.MultipartFile;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.dto.PacienteDTO;
import com.sysmei.keys.Keys;
import com.sysmei.model.Paciente;
import com.sysmei.service.DocumentsService;
import com.sysmei.service.PacienteService;
import com.sysmei.service.S3Service;

@RestController
@RequestMapping(value = Keys.PACIENTE)
public class PacienteController {

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private DocumentsService documentsService;

	@RequestMapping(method = RequestMethod.GET, params = { Keys.PARAM_LOGIN })
	public ResponseEntity<List<PacienteDTO>> findAll(@RequestParam(name = Keys.PARAM_LOGIN) String login) {
		List<Paciente> list = pacienteService.getPacientesWithLogin(login);
		List<PacienteDTO> listDto = list.stream().map(obj -> new PacienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@GetMapping(Keys.ID)
	public ResponseEntity<Paciente> getById(@PathVariable Long id) {
		Paciente dto = pacienteService.getById(id);
		return ResponseEntity.ok().body(dto);
	}

	@RequestMapping(value = Keys.PAGE, method = RequestMethod.GET)
	public ResponseEntity<Page<PacienteDTO>> findPage(@RequestParam(value = Keys.PARAM_PAGE, defaultValue = "0") Integer page,
			@RequestParam(value = Keys.PARAM_LINES_PER_PAGE, defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = Keys.PARAM_ORDER_BY, defaultValue = "nome") String orderBy,
			@RequestParam(value = Keys.PARAM_DIRECTION, defaultValue = "ASC") String direction) {
		Page<Paciente> list = pacienteService.findPage(page, linesPerPage, orderBy, direction);
		Page<PacienteDTO> listDto = list.map(obj -> new PacienteDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody NewPacienteDTO dto) {
		Paciente paciente = pacienteService.fromDTO(dto);

		return new ResponseEntity<>(pacienteService.insert(paciente), HttpStatus.CREATED);
	}

	@RequestMapping(value = Keys.ID, method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody NewPacienteDTO objDto, @PathVariable Long id) {
		Paciente obj = pacienteService.fromDTO(objDto);
		obj.setId(id);
		obj = pacienteService.update(obj);
		return ResponseEntity.noContent().build();

	}

	@RequestMapping(value = Keys.ID, method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		pacienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = Keys.PACIENTE_ID, method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@PathVariable Long id,
			@RequestParam(name = Keys.PARAM_FILE) MultipartFile file) {
		URI uri = pacienteService.uploadProfilePicture(id, file);
		return ResponseEntity.created(uri).build();
	}
	@RequestMapping(value = Keys.DELETE, method = RequestMethod.DELETE)
	    public ResponseEntity<String> deleteFile(@RequestParam(value= Keys.PARAM_FILE_NAME) String keyName, @RequestParam(value= Keys.PARAM_FILE_ID) Long fileId) {
	        s3Service.deleteFile(keyName);
	        documentsService.delete(fileId);
	        final String response = "[" + keyName + "] detelado com sucesso.";
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
}
