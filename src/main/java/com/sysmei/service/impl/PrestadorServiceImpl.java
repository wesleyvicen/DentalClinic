package com.sysmei.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sysmei.dto.PrestadorDTO;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.model.Prestador;
import com.sysmei.repository.PrestadorRepository;
import com.sysmei.service.PrestadorService;
import com.sysmei.service.exception.DataIntegrityException;

@Service
public class PrestadorServiceImpl implements PrestadorService {

	@Autowired
	private PrestadorRepository prestadorRepository;

	@Autowired
	private UsuarioServiceImpl usuarioService;

	public List<Prestador> getPrestadorWithLogin(String login) {
		List<Prestador> list = prestadorRepository.getPrestadorWithLogin(login);
		return list;
	}

	public Prestador getById(Long id) {
		Optional<Prestador> obj = prestadorRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + id + ", Tipo: " + Prestador.class.getName()));
	}

	public Prestador getByTelefone(String telefone) {
		Optional<Prestador> obj = prestadorRepository.findByTelefone(telefone);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + telefone + ", Tipo: " + Prestador.class.getName()));
	}

	public Prestador update(Prestador obj) {
		Prestador newObj = getById(obj.getId());
		updateData(newObj, obj);
		return prestadorRepository.save(newObj);
	}

	@Transactional
	public Prestador insert(Prestador prestador) {
		prestador = prestadorRepository.save(prestador);
		return prestador;
	}

	public void delete(Long id) {
		prestadorRepository.findById(id);
		try {

			prestadorRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque existe entidades relacionadas");
		}
	}

	private void updateData(Prestador newObj, Prestador obj) {

		newObj.setNome(obj.getNome() == null ? newObj.getNome() : obj.getNome());
		newObj.setTelefone(obj.getTelefone() == null ? newObj.getTelefone() : obj.getTelefone());
	}

	@Transactional
	public Prestador fromDTO(PrestadorDTO dto) {
		Prestador prestador = new Prestador(dto.getNome(), dto.getTelefone());
		prestador.setUsuario(usuarioService.getUsuarioWithLogin(dto.getLogin_usuario()));
		return prestador;
	}

}
