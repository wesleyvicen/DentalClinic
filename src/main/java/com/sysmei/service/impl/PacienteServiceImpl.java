package com.sysmei.service.impl;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.model.DocumentUrl;
import com.sysmei.model.Paciente;
import com.sysmei.repository.PacienteRepository;
import com.sysmei.security.UserSS;
import com.sysmei.service.PacienteService;
import com.sysmei.service.exception.AuthorizationException;
import com.sysmei.service.exception.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PacienteServiceImpl implements PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private UsuarioServiceImpl usuarioService;

	@Autowired
	private S3ServiceImpl s3Service;

	@Autowired
	private ImageServiceImpl imageService;

//	@Transactional(readOnly = true)
//	public List<Paciente> findAll() {
//		return pacienteRepository.findAll();
//	}

	public List<Paciente> getPacientesWithLogin(String login) {
		List<Paciente> list = pacienteRepository.getPacientesWithLogin(login);
		return list;
	}

	public Paciente getById(Long id) {
		Optional<Paciente> obj = pacienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + id + ", Tipo: " + Paciente.class.getName()));
	}

	public Paciente update(Paciente obj) {
		Paciente newObj = getById(obj.getId());
		updateData(newObj, obj);
		return pacienteRepository.save(newObj);
	}

	@Transactional
	public Paciente insert(Paciente paciente) {
		paciente = pacienteRepository.save(paciente);
		return paciente;
	}

	@Transactional
	public Page<Paciente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return pacienteRepository.findAll(pageRequest);
	}

	public void delete(Long id) {
		pacienteRepository.findById(id);
		try {

			pacienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque existe entidades relacionadas");
		}
	}

	private void updateData(Paciente newObj, Paciente obj) {

		newObj.setNome(obj.getNome() == null ? newObj.getNome() : obj.getNome());
		newObj.setSocialName(obj.getSocialName() == null ? newObj.getSocialName() : obj.getSocialName());
		newObj.setEmail(obj.getEmail() == null ? newObj.getEmail() : obj.getEmail());
		newObj.setTelefone1(obj.getTelefone1() == null ? newObj.getTelefone1() : obj.getTelefone1());
		newObj.setTelefone2(obj.getTelefone2() == null ? newObj.getTelefone2() : obj.getTelefone2());
		newObj.setTelefone3(obj.getTelefone3() == null ? newObj.getTelefone3() : obj.getTelefone3());
		newObj.setNascimento(obj.getNascimento() == null ? newObj.getNascimento() : obj.getNascimento());
		newObj.setResponsavel(obj.getResponsavel() == null ? newObj.getResponsavel() : obj.getResponsavel());
		newObj.setSexo(obj.getSexo() == null ? newObj.getSexo() : obj.getSexo());
		newObj.setEstadoCivil(obj.getEstadoCivil() == null ? newObj.getEstadoCivil() : obj.getEstadoCivil());
		newObj.setIndicacao(obj.getIndicacao() == null ? newObj.getIndicacao() : obj.getIndicacao());
		newObj.setPlanoSaude(obj.getPlanoSaude() == null ? newObj.getPlanoSaude() : obj.getPlanoSaude());
		newObj.setConvenio(obj.getConvenio() == null ? newObj.getConvenio() : obj.getConvenio());
		newObj.setRg(obj.getRg() == null ? newObj.getRg() : obj.getRg());
		newObj.setCpf(obj.getCpf() == null ? newObj.getCpf() : obj.getCpf());
		newObj.setOcupacao(obj.getOcupacao() == null ? newObj.getOcupacao() : obj.getOcupacao());
		newObj.setEndereco(obj.getEndereco() == null ? newObj.getEndereco() : obj.getEndereco());
		newObj.setEnderecoNum(obj.getEnderecoNum() == null ? newObj.getEnderecoNum() : obj.getEnderecoNum());
		newObj.setBairro(obj.getBairro() == null ? newObj.getBairro() : obj.getBairro());
		newObj.setCidade(obj.getCidade() == null ? newObj.getCidade() : obj.getCidade());
		newObj.setEstado(obj.getEstado() == null ? newObj.getEstado() : obj.getEstado());
		newObj.setCep(obj.getCep() == null ? newObj.getCep() : obj.getCep());

	}

	@Transactional
	public Paciente fromDTO(NewPacienteDTO dto) {
		Paciente paciente = new Paciente(dto.getNome(), dto.getSocialName(), dto.getEmail(), dto.getTelefone1(),
				dto.getTelefone2(), dto.getTelefone3(), dto.getNascimento(), dto.getResponsavel(), dto.getSexo(),
				dto.getEstadoCivil(), dto.getIndicacao(), dto.getPlanoSaude(), dto.getConvenio(), dto.getRg(),
				dto.getCpf(), dto.getOcupacao(), dto.getEndereco(), dto.getEnderecoNum(), dto.getBairro(),
				dto.getCidade(), dto.getEstado(), dto.getCep());
		paciente.setUsuario(usuarioService.getUsuarioWithLogin(dto.getLogin_usuario()));
		return paciente;
	}

	@Transactional
	public URI uploadProfilePicture(Long id, MultipartFile multipartFile) {
		UserSS user = UsuarioServiceImpl.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}

		Paciente paciente = pacienteRepository.findById(id).orElse(null);

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		Random random = new Random();
		String fileName = user.getUsername() + "_" + random.nextInt(1000) + ".jpg";
		URI uri = s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		DocumentUrl documentsUrl = new DocumentUrl(uri.toString(), paciente);
		paciente.getDocumentsUrl().add(documentsUrl);
		pacienteRepository.save(paciente);

		return uri;
	}

}
