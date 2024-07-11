package com.sysmei.service.impl;

import com.sysmei.dto.NewPacienteDTO;
import com.sysmei.dto.PacienteDTO;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.model.DocumentUrl;
import com.sysmei.model.Paciente;
import com.sysmei.repository.PacienteRepository;
import com.sysmei.security.UserSS;
import com.sysmei.service.PacienteService;
import com.sysmei.service.exception.AuthorizationException;
import com.sysmei.service.exception.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    @Lazy
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private S3ServiceImpl s3Service;

    @Autowired
    private ImageServiceImpl imageService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mail;

    @Cacheable(value = "pacientes", key = "#login")
    public List<Paciente> getPacientesWithLogin(String login) {
        return pacienteRepository.getPacientesWithLogin(login);
    }

    @Cacheable(value = "paciente", key = "#id")
    public Paciente getById(Long id) {
        Optional<Paciente> obj = pacienteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto Não encontrado! ID: " + id + ", Tipo: " + Paciente.class.getName()));
    }

    @Cacheable(value = "paciente", key = "{#id, #loginUsuario}")
    public Paciente getByIdAndLoginUsuario(Long id, String loginUsuario) {
        Optional<Paciente> obj = pacienteRepository.findByIdAndLoginUsuario(id, loginUsuario);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto Não encontrado! ID: " + id + ", Tipo: " + Paciente.class.getName()));
    }

    @CacheEvict(value = "paciente", key = "#obj.id")
    public Paciente update(Paciente obj) {
        Paciente newObj = getById(obj.getId());
        updateData(newObj, obj);
        return pacienteRepository.save(newObj);
    }

    @CacheEvict(value = "pacientes", allEntries = true)
    @Transactional
    public Paciente insert(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Cacheable(value = "pacientesPage", key = "{#page, #linesPerPage, #orderBy, #direction}")
    @Transactional(readOnly = true)
    public Page<Paciente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return pacienteRepository.findAll(pageRequest);
    }

    @CacheEvict(value = "paciente", key = "#id")
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
        Paciente paciente =
                new Paciente(dto.getNome(), dto.getSocialName(), dto.getEmail(), dto.getTelefone1(),
                        dto.getTelefone2(), dto.getTelefone3(), dto.getNascimento(), dto.getResponsavel(),
                        dto.getSexo(), dto.getEstadoCivil(), dto.getIndicacao(), dto.getPlanoSaude(),
                        dto.getConvenio(), dto.getRg(), dto.getCpf(), dto.getOcupacao(), dto.getEndereco(),
                        dto.getEnderecoNum(), dto.getBairro(), dto.getCidade(), dto.getEstado(), dto.getCep());
        paciente.setUsuario(usuarioService.getUsuarioWithLogin(dto.getLogin_usuario()));
        return paciente;
    }

    @CacheEvict(value = "paciente", key = "#id")
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

    @Override
    @CacheEvict(value = "pacientes", allEntries = true)
    public PacienteDTO insert(PacienteDTO pacienteDTO, boolean isInternal) {
        Paciente paciente = new Paciente(
                pacienteDTO.getNome(),
                pacienteDTO.getEmail(),
                pacienteDTO.getTelefone1(),
                isInternal ? null : new BCryptPasswordEncoder().encode(pacienteDTO.getSenha()),
                pacienteDTO.getLogin_usuario()
        );
        pacienteRepository.save(paciente);

        if (isInternal) {
            sendPasswordSetupEmail(paciente.getEmail());
        }

        return new PacienteDTO(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone1(), null);
    }

    @Override
    @CacheEvict(value = "pacientes", allEntries = true)
    public PacienteDTO register(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente(
                pacienteDTO.getNome(),
                pacienteDTO.getEmail(),
                pacienteDTO.getTelefone1(),
                pacienteDTO.getLogin_usuario(),
                new BCryptPasswordEncoder().encode(pacienteDTO.getSenha()) // Criptografar senha
        );
        pacienteRepository.save(paciente);
        return new PacienteDTO(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone1(), null);
    }

    @Override
    @Cacheable(value = "paciente", key = "#email")
    public PacienteDTO login(String email, String senha) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findByEmail(email);
        if (!optionalPaciente.isPresent()) {
            throw new RuntimeException("Paciente não encontrado com este email");
        }

        Paciente paciente = optionalPaciente.get();
        if (paciente != null && new BCryptPasswordEncoder().matches(senha, paciente.getSenha())) {
            return new PacienteDTO(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone1(), null);
        }
        throw new RuntimeException("Email ou senha inválidos");
    }

    @Override
    @Transactional
    public void sendPasswordSetupEmail(String email) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findByEmail(email);
        if (!optionalPaciente.isPresent()) {
            throw new RuntimeException("Paciente não encontrado com este email");
        }

        Paciente paciente = optionalPaciente.get();
        String token = UUID.randomUUID().toString();
        paciente.setResetPasswordToken(token);
        pacienteRepository.save(paciente);

        String setupPasswordLink = "http://localhost:8080/setup_password?token=" + token;
        sendEmail(paciente.getEmail(), setupPasswordLink);
    }

    private void sendEmail(String email, String setupPasswordLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("no-reply@sysmei.com", "Sysmei Support");
            helper.setTo(email);

            String subject = "Configuração de senha";
            String content = "<p>Olá,</p>"
                    + "<p>Você foi registrado no sistema Sysmei.</p>"
                    + "<p>Clique no link abaixo para configurar sua senha:</p>"
                    + "<p><a href=\"" + setupPasswordLink + "\">Configurar minha senha</a></p>"
                    + "<br>"
                    + "<p>Ignore este e-mail se você já configurou sua senha, "
                    + "ou não fez esta solicitação.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findByResetPasswordToken(token);
        if (!optionalPaciente.isPresent()) {
            throw new RuntimeException("Token inválido");
        }

        Paciente paciente = optionalPaciente.get();
        paciente.setSenha(new BCryptPasswordEncoder().encode(newPassword));
        paciente.setResetPasswordToken(null);
        pacienteRepository.save(paciente);
    }

}
