package com.dentalclinic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dentalclinic.model.Agenda;
import com.dentalclinic.service.AgendaService;

@RestController
@RequestMapping(value = "/agenda")
public class AgendaController {

	@Autowired
	private AgendaService agendaService;

	@RequestMapping(method = RequestMethod.GET, params = {"login"})
	public ResponseEntity<List<Agenda>> findAll(@RequestParam(name = "login") String login) {
		List<Agenda> list = agendaService.getAgendasWithLogin(login);
		return ResponseEntity.ok().body(list);
	}
//	@RequestMapping(method = RequestMethod.GET, params = {"start", "end" })
//	public ResponseEntity<?> findByDateBetween( @RequestParam("start") String start, @RequestParam("end") String end){
//		
//			List<Agenda> list = agendaService.findByDate(start, end);
//			
//			return ResponseEntity.ok().body(list);
//	}

	@PostMapping
	public ResponseEntity<Agenda> insert(@RequestBody Agenda dto) {
		return new ResponseEntity<>(agendaService.insert(dto), HttpStatus.CREATED);
	}
}
