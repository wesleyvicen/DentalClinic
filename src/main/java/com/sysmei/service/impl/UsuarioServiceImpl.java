package com.sysmei.service.impl;

import com.sysmei.dto.LoginDto;
import com.sysmei.dto.SessaoDto;
import com.sysmei.dto.TokenDto;
import com.sysmei.dto.UsuarioDto;
import com.sysmei.exceptions.ObjectNotFoundException;
import com.sysmei.mapper.UsuarioMapper;
import com.sysmei.model.Prestador;
import com.sysmei.model.Usuario;
import com.sysmei.repository.UsuarioRepository;
import com.sysmei.security.JWTUtil;
import com.sysmei.security.UserSS;
import com.sysmei.service.PrestadorService;
import com.sysmei.service.UsuarioService;
import com.sysmei.service.exception.AuthorizationException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	private S3ServiceImpl s3Service;

	@Autowired
	private ImageServiceImpl imageService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String mail;
//
//	@Value("${img.prefix.client.profile}")
//	private String prefix;

	// encripta a senha digitada pelo usuário
	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private PrestadorService prestadorService;

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
			incluirUsuarioConta(usuario);
			// sendVerificationEmail(usuario, "https://sysmei.com");
			Prestador prestador = new Prestador();
			prestador.setNome("Geral");
			prestador.setTelefone("");
			prestador.setUsuario(usuario);
			prestadorService.insert(prestador);
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

	private void sendVerificationEmail(Usuario user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getLogin();
		String fromAddress = mail;
		String senderName = "Sysmei";
		String subject = "Por favor, verifique o seu registro";
		String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"width:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\r\n"
				+ " <head> \r\n" + "  <meta charset=\"UTF-8\"> \r\n"
				+ "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"> \r\n"
				+ "  <meta name=\"x-apple-disable-message-reformatting\"> \r\n"
				+ "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> \r\n"
				+ "  <meta content=\"telephone=no\" name=\"format-detection\"> \r\n"
				+ "  <title>Nova mensagem</title> \r\n" + "  <!--[if (mso 16)]>\r\n"
				+ "    <style type=\"text/css\">\r\n" + "    a {text-decoration: none;}\r\n" + "    </style>\r\n"
				+ "    <![endif]--> \r\n"
				+ "  <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--> \r\n"
				+ "  <!--[if gte mso 9]>\r\n" + "<xml>\r\n" + "    <o:OfficeDocumentSettings>\r\n"
				+ "    <o:AllowPNG></o:AllowPNG>\r\n" + "    <o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
				+ "    </o:OfficeDocumentSettings>\r\n" + "</xml>\r\n" + "<![endif]--> \r\n"
				+ "  <!--[if !mso]><!-- --> \r\n"
				+ "  <link href=\"https://fonts.googleapis.com/css?family=Lato:400,400i,700,700i\" rel=\"stylesheet\"> \r\n"
				+ "  <!--<![endif]--> \r\n" + "  <style type=\"text/css\">\r\n" + "#outlook a {\r\n"
				+ "	padding:0;\r\n" + "}\r\n" + ".ExternalClass {\r\n" + "	width:100%;\r\n" + "}\r\n"
				+ ".ExternalClass,\r\n" + ".ExternalClass p,\r\n" + ".ExternalClass span,\r\n"
				+ ".ExternalClass font,\r\n" + ".ExternalClass td,\r\n" + ".ExternalClass div {\r\n"
				+ "	line-height:100%;\r\n" + "}\r\n" + ".es-button {\r\n" + "	mso-style-priority:100!important;\r\n"
				+ "	text-decoration:none!important;\r\n" + "}\r\n" + "a[x-apple-data-detectors] {\r\n"
				+ "	color:inherit!important;\r\n" + "	text-decoration:none!important;\r\n"
				+ "	font-size:inherit!important;\r\n" + "	font-family:inherit!important;\r\n"
				+ "	font-weight:inherit!important;\r\n" + "	line-height:inherit!important;\r\n" + "}\r\n"
				+ ".es-desk-hidden {\r\n" + "	display:none;\r\n" + "	float:left;\r\n" + "	overflow:hidden;\r\n"
				+ "	width:0;\r\n" + "	max-height:0;\r\n" + "	line-height:0;\r\n" + "	mso-hide:all;\r\n" + "}\r\n"
				+ "[data-ogsb] .es-button {\r\n" + "	border-width:0!important;\r\n"
				+ "	padding:15px 25px 15px 25px!important;\r\n" + "}\r\n" + "[data-ogsb] .es-button.es-button-1 {\r\n"
				+ "	padding:15px 30px!important;\r\n" + "}\r\n"
				+ "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120%!important } h2 { font-size:26px!important; text-align:center; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; border-width:15px 25px 15px 25px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }\r\n"
				+ "</style> \r\n" + " </head> \r\n"
				+ " <body style=\"width:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"> \r\n"
				+ "  <div class=\"es-wrapper-color\" style=\"background-color:#F4F4F4\"> \r\n"
				+ "   <!--[if gte mso 9]>\r\n"
				+ "			<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\r\n"
				+ "				<v:fill type=\"tile\" color=\"#f4f4f4\"></v:fill>\r\n" + "			</v:background>\r\n"
				+ "		<![endif]--> \r\n"
				+ "   <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\"> \r\n"
				+ "     <tr class=\"gmail-fix\" height=\"0\" style=\"border-collapse:collapse\"> \r\n"
				+ "      <td style=\"padding:0;Margin:0\"> \r\n"
				+ "       <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;width:600px\"> \r\n"
				+ "         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "          <td cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"padding:0;Margin:0;line-height:1px;min-width:600px\" height=\"0\"><img src=\"https://eoivll.stripocdn.email/content/guids/CABINET_837dc1d79e3a5eca5eb1609bfe9fd374/images/41521605538834349.png\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;max-height:0px;min-height:0px;min-width:600px;width:600px\" alt width=\"600\" height=\"1\"></td> \r\n"
				+ "         </tr> \r\n" + "       </table></td> \r\n" + "     </tr> \r\n"
				+ "     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "      <td valign=\"top\" style=\"padding:0;Margin:0\"> \r\n"
				+ "       <table class=\"es-header\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:#FFA73B;background-repeat:repeat;background-position:center top\"> \r\n"
				+ "         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "          <td align=\"center\" bgcolor=\"#673ab7\" style=\"padding:0;Margin:0;background-color:#673AB7\"> \r\n"
				+ "           <table class=\"es-header-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\"> \r\n"
				+ "             <tr style=\"border-collapse:collapse\"> \r\n"
				+ "              <td align=\"left\" style=\"Margin:0;padding-bottom:10px;padding-left:10px;padding-right:10px;padding-top:20px\"> \r\n"
				+ "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                 <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:580px\"> \r\n"
				+ "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td align=\"center\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:25px;padding-bottom:25px;font-size:0px\"><a href=\"https://sysmei.com\" target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#111111;font-size:14px\"><img src=\"https://eoivll.stripocdn.email/content/guids/CABINET_1f5f7784fbf2ad371f533bbe6b157abb/images/30261620873575322.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" height=\"64\"></a></td> \r\n"
				+ "                     </tr> \r\n" + "                   </table></td> \r\n"
				+ "                 </tr> \r\n" + "               </table></td> \r\n" + "             </tr> \r\n"
				+ "           </table></td> \r\n" + "         </tr> \r\n" + "       </table> \r\n"
				+ "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \r\n"
				+ "         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "          <td style=\"padding:0;Margin:0;background-color:#673AB7\" bgcolor=\"#673ab7\" align=\"center\"> \r\n"
				+ "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \r\n"
				+ "             <tr style=\"border-collapse:collapse\"> \r\n"
				+ "              <td align=\"left\" style=\"padding:0;Margin:0\"> \r\n"
				+ "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                 <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \r\n"
				+ "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FFFFFF;border-radius:4px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\"> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td align=\"center\" style=\"Margin:0;padding-bottom:5px;padding-left:30px;padding-right:30px;padding-top:35px\"><h1 style=\"Margin:0;line-height:58px;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;font-size:48px;font-style:normal;font-weight:normal;color:#111111\">Bem Vindo!</h1></td> \r\n"
				+ "                     </tr> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td bgcolor=\"#ffffff\" align=\"center\" style=\"Margin:0;padding-top:5px;padding-bottom:5px;padding-left:20px;padding-right:20px;font-size:0\"> \r\n"
				+ "                       <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                          <td style=\"padding:0;Margin:0;border-bottom:1px solid #FFFFFF;background:#FFFFFF none repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td> \r\n"
				+ "                         </tr> \r\n" + "                       </table></td> \r\n"
				+ "                     </tr> \r\n" + "                   </table></td> \r\n"
				+ "                 </tr> \r\n" + "               </table></td> \r\n" + "             </tr> \r\n"
				+ "           </table></td> \r\n" + "         </tr> \r\n" + "       </table> \r\n"
				+ "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \r\n"
				+ "         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "          <td align=\"center\" style=\"padding:0;Margin:0\"> \r\n"
				+ "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \r\n"
				+ "             <tr style=\"border-collapse:collapse\"> \r\n"
				+ "              <td align=\"left\" style=\"padding:0;Margin:0\"> \r\n"
				+ "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                 <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \r\n"
				+ "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:4px;background-color:#FFFFFF\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\"> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"left\" style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px;text-align:center\">Oi, [[name]]!</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Estamos muito felizes por você se registrar. Primeiro, você precisa confirmar sua conta. Basta pressionar o botão abaixo.</p></td> \r\n"
				+ "                     </tr> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td align=\"center\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:35px;padding-bottom:35px\"><span class=\"es-button-border\" style=\"border-style:solid;border-color:#865DDD;background:#673AB7;border-width:1px;display:inline-block;border-radius:2px;width:auto\"><a href=\"[[URL]]\" class=\"es-button es-button-1\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#FFFFFF;font-size:20px;border-style:solid;border-color:#673AB7;border-width:15px 30px;display:inline-block;background:#673AB7;border-radius:2px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-weight:normal;font-style:normal;line-height:24px;width:auto;text-align:center\">Confirmar</a></span></td> \r\n"
				+ "                     </tr> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td class=\"es-m-txt-l\" align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Se isso não funcionar, copie e cole o seguinte link no seu navegador:</p></td> \r\n"
				+ "                     </tr> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td class=\"es-m-txt-l\" align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:30px;padding-right:30px\"><a target=\"_blank\" href=\"[[URL]]\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#673AB7;font-size:18px\">[[URL]]</a></td> \r\n"
				+ "                     </tr> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td class=\"es-m-txt-l\" align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Caso seja um erro, desconsidere!</p></td> \r\n"
				+ "                     </tr> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td class=\"es-m-txt-l\" align=\"left\" style=\"Margin:0;padding-top:20px;padding-left:30px;padding-right:30px;padding-bottom:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Saúde,<br>Equipe Sysmei!</p></td> \r\n"
				+ "                     </tr> \r\n" + "                   </table></td> \r\n"
				+ "                 </tr> \r\n" + "               </table></td> \r\n" + "             </tr> \r\n"
				+ "           </table></td> \r\n" + "         </tr> \r\n" + "       </table> \r\n"
				+ "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \r\n"
				+ "         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "          <td align=\"center\" style=\"padding:0;Margin:0\"> \r\n"
				+ "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \r\n"
				+ "             <tr style=\"border-collapse:collapse\"> \r\n"
				+ "              <td align=\"left\" style=\"padding:0;Margin:0\"> \r\n"
				+ "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                 <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \r\n"
				+ "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                     <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                      <td align=\"center\" style=\"Margin:0;padding-top:10px;padding-bottom:20px;padding-left:20px;padding-right:20px;font-size:0\"> \r\n"
				+ "                       <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n"
				+ "                         <tr style=\"border-collapse:collapse\"> \r\n"
				+ "                          <td style=\"padding:0;Margin:0;border-bottom:1px solid #F4F4F4;background:#FFFFFF none repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td> \r\n"
				+ "                         </tr> \r\n" + "                       </table></td> \r\n"
				+ "                     </tr> \r\n" + "                   </table></td> \r\n"
				+ "                 </tr> \r\n" + "               </table></td> \r\n" + "             </tr> \r\n"
				+ "           </table></td> \r\n" + "         </tr> \r\n" + "       </table></td> \r\n"
				+ "     </tr> \r\n" + "   </table> \r\n" + "  </div>  \r\n" + " </body>\r\n" + "</html>";

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