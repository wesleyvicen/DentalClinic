package com.dentalclinic.service;

import org.springframework.mail.SimpleMailMessage;

import com.dentalclinic.model.Usuario;

public interface EmailService {

    void sendConfirmationEmail(Usuario obj);

    void sendEmail(SimpleMailMessage msg);

    void sendNewPasswordEmail(Usuario usuario, String newPass);
}