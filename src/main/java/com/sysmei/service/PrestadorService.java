package com.sysmei.service;

import com.sysmei.dto.PrestadorDTO;
import com.sysmei.model.Prestador;

import java.util.List;

public interface PrestadorService {

    public List<Prestador> getPrestadorWithLogin(String login);

    public Prestador getById(Long id);

    public Prestador getByTelefone(String telefone);

    public Prestador update(Prestador obj);

	public Prestador insert(Prestador prestador);

	public void delete(Long id);

	public Prestador fromDTO(PrestadorDTO dto);
}
