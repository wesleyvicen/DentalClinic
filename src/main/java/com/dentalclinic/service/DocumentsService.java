package com.dentalclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.dentalclinic.repository.DocumentsRepository;
import com.dentalclinic.service.exception.DataIntegrityException;

@Service
public class DocumentsService {

	@Autowired
	private DocumentsRepository documentsRepository;
	public void delete(Long id) {
		documentsRepository.findById(id);
		try {

			documentsRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque existe entidades relacionadas");
		}
	}

}
