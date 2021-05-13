package com.dentalclinic.service;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dentalclinic.dto.LoginDto;
import com.dentalclinic.dto.SessaoDto;
import com.dentalclinic.dto.TokenDto;
import com.dentalclinic.dto.UsuarioDto;
import com.dentalclinic.exceptions.ObjectNotFoundException;
import com.dentalclinic.mapper.UsuarioMapper;
import com.dentalclinic.model.Usuario;
import com.dentalclinic.repository.UsuarioRepository;
import com.dentalclinic.security.JWTUtil;
import com.dentalclinic.security.UserSS;
import com.dentalclinic.service.exception.AuthorizationException;

import net.bytebuddy.utility.RandomString;

/**
 * @author B�rbara Rodrigues, Gabriel Botelho, Guilherme Cruz, Lucas Caputo,
 *         Renan Alencar, Wesley Vicente
 *
 */
@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;
	
	 @Autowired
	    private JavaMailSender mailSender;
//
//	@Value("${img.prefix.client.profile}")
//	private String prefix;

	// encripta a senha digitada pelo usuário
	@Autowired
	private BCryptPasswordEncoder pe;

	@Transactional
	public UsuarioDto addUsuario(UsuarioDto usuarioDto) throws UnsupportedEncodingException, MessagingException {
		Usuario usuario = new Usuario();
		usuario.setTelefone(usuarioDto.getTelefone());
		usuario.setLogin(usuarioDto.getLogin());
		usuario.setNome(usuarioDto.getNome());
		usuario.setSenha(pe.encode(usuarioDto.getSenha()));
		usuario.setStatus(usuarioDto.getStatus());
		usuario.setEnabled(false);
		String randomCode = RandomString.make(64);
		usuario.setVerificationCode(randomCode);
		if (!existsUsuarioWithLogin(usuario.getLogin())) {
			usuarioRepository.save(usuario);
			incluirUsuarioConta(usuario);
			sendVerificationEmail(usuario, "https://sysmei.com");
			return usuarioDto;
		} else {
			throw new IllegalStateException();
		}
	}

	@Transactional
	private void incluirUsuarioConta(Usuario usuario) {

		usuarioRepository.save(usuario);

	}

	@Transactional
	public Usuario getUsuarioWithLoginAndSenha(String login, String senha) {
		Usuario usuario = getUsuarioWithLogin(login);
		if (usuario == null) {
			throw new IllegalArgumentException("Usuário não existe");
		} else if (pe.matches(senha, usuario.getSenha())) {
			return usuario;
		} else {
			throw new IllegalArgumentException("Senha incorreta!");
		}
	}

	@Transactional
	public Usuario getUsuarioWithLogin(String login) {
		return usuarioRepository.getUsuarioWithLogin(login);
	}

	@Transactional
	public boolean existsUsuarioWithLogin(String login) {
		Usuario usuario = usuarioRepository.getUsuarioWithLogin(login);
		if (usuario != null) {
			return true;
		} else {
			return false;
		}

	}

	@Transactional
	public Usuario search(Integer id) {
		Optional<Usuario> obj = usuarioRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto Não encontrado! ID: " + id + ", Tipo: " + Usuario.class.getName()));
	}

	@Transactional
	public List<UsuarioDto> findAll(Integer id) {
		List<Usuario> list = usuarioRepository.findUsuarioAndConta(id);
		return list.stream().map(x -> new UsuarioDto(x)).collect(Collectors.toList());
	}

	@Transactional
	public SessaoDto logar(LoginDto loginDto) {
		SessaoDto sessaoDto = new SessaoDto();

		UsuarioMapper usuarioMapper = new UsuarioMapper();
		Usuario usuario = getUsuarioWithLoginAndSenha(loginDto.getUsuario(), loginDto.getSenha());

		if (usuario == null) {
			throw new IllegalArgumentException("Usuário ou senha errados!");
		}

		TokenDto tokenDto = jwtUtil.getToken(loginDto.getUsuario());
		LocalDateTime agora = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String dataInicio = agora.format(formatter);
		String dataFim = agora.plusMinutes(60).format(formatter);
		sessaoDto.setToken(tokenDto.getToken());
		sessaoDto.setDataInicio(dataInicio);
		sessaoDto.setDataFim(dataFim);

		sessaoDto.setUsuario(usuarioMapper.getUsuarioDtoFromEntity(usuario));

		return sessaoDto;
	}

	/***
	 * 
	 * @return
	 */
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}

		Usuario usuario = usuarioRepository.findById(user.getId()).orElse(null);

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String fileName = user.getUsername() + "_" + user.getId() + ".jpg";
		URI uri = s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		usuario.setImageUrl(uri.toString());
		usuarioRepository.save(usuario);

		return uri;
	}
	
	public void register(Usuario user, String siteURL) {
	     
    }
     
	private void sendVerificationEmail(Usuario user, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
	    String toAddress = user.getLogin();
	    String fromAddress = "contato@sysmei.com";
	    String senderName = "Sysmei";
	    String subject = "Por favor, verifique o seu registro";
	    String content = "Olá, [[name]],<br>"
	            + "Clique no link abaixo para verificar o seu registro:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFICAR</a></h3>"
	            + "Obrigada,<br>"
	            + "Sysmei.com.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getNome());
	    String verifyURL = siteURL + "/user/token?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	     
	}
	
	public boolean verificarUser(String verificationCode) {
	    Usuario user = usuarioRepository.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        usuarioRepository.save(user);
	         
	        return true;
	    }
	     
	}
}