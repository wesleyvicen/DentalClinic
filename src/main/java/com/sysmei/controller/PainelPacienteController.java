package com.sysmei.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysmei.dto.PacienteDTO;
import com.sysmei.keys.RotasKeys;
import com.sysmei.service.impl.PacienteServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = RotasKeys.PACIENTE)
@Tag(name = "Painel do Paciente Controller", description = "Gerenciamento de pacientes")
public class PainelPacienteController {

  @Autowired
  private PacienteServiceImpl pacienteService;

  /**
   * Registrar um novo paciente.
   * 
   * @param pacienteDTO Dados do novo paciente
   * @return Retorna os dados do paciente registrado
   */
  @Operation(summary = "Registrar um novo paciente", description = "Endpoint para registrar um novo paciente no sistema.")
  @PostMapping("/register")
  public ResponseEntity<PacienteDTO> register(@RequestBody PacienteDTO pacienteDTO) {
      PacienteDTO newPaciente = pacienteService.register(pacienteDTO);
      return ResponseEntity.ok(newPaciente);
  }

  /**
   * Criar um paciente internamente.
   * 
   * @param pacienteDTO Dados do novo paciente
   * @param isInternal Indica se o paciente está sendo criado internamente
   * @return Retorna os dados do paciente criado
   */
  @Operation(summary = "Criar um paciente internamente", description = "Endpoint para criar um paciente internamente no sistema.")
  @PostMapping("/create")
  public ResponseEntity<PacienteDTO> create(@RequestBody PacienteDTO pacienteDTO, @RequestParam boolean isInternal) {
      PacienteDTO newPaciente = pacienteService.insert(pacienteDTO, isInternal);
      return ResponseEntity.ok(newPaciente);
  }

  /**
   * Login de paciente.
   * 
   * @param loginData Mapa contendo o email e a senha do paciente
   * @return Retorna os dados do paciente logado
   */
  @Operation(summary = "Login de paciente", description = "Endpoint para o login de um paciente no sistema.")
  @PostMapping("/login")
  public ResponseEntity<PacienteDTO> login(@RequestBody Map<String, String> loginData) {
      PacienteDTO paciente = pacienteService.login(loginData.get("email"), loginData.get("senha"));
      return ResponseEntity.ok(paciente);
  }

  /**
   * Enviar email para configuração de senha.
   * 
   * @param email Email do paciente
   * @return Retorna uma resposta vazia com status OK
   */
  @Operation(summary = "Enviar email para configuração de senha", description = "Endpoint para enviar um email ao paciente com um link para configurar a senha.")
  @PostMapping("/send-password-setup")
  public ResponseEntity<Void> sendPasswordSetupEmail(@RequestParam String email) {
      pacienteService.sendPasswordSetupEmail(email);
      return ResponseEntity.ok().build();
  }

  /**
   * Redefinir senha do paciente.
   * 
   * @param token Token de redefinição de senha
   * @param newPassword Nova senha do paciente
   * @return Retorna uma resposta vazia com status OK
   */
  @Operation(summary = "Redefinir senha do paciente", description = "Endpoint para redefinir a senha do paciente usando um token de redefinição.")
  @PostMapping("/reset_password")
  public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
      pacienteService.resetPassword(token, newPassword);
      return ResponseEntity.ok().build();
  }
}
