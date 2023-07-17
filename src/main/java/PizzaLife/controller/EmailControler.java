/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@Controller
@RequestMapping("/correo")
public class EmailControler {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailControler(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/enviar")
    public String enviarCorreo(
            @RequestParam("to") String destinatario,
            @RequestParam("subject") String asunto,
            @RequestParam("content") String contenido,
            @RequestParam("nombre") String nombre,
            @RequestParam("mensaje") String mensaje,
            Model model
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            String correoHtml = "<html><body style=\"font-family: Arial, sans-serif;\">"
                    + "<h1 style=\"color: #333333;\">Hola, soy " + nombre + "</h1>"
                    + "<p style=\"color: #666666;\">" + mensaje + "</p>"
                    + "<p style=\"color: #666666;\">Contenido personalizado: " + contenido + "</p>"
                    + "</body></html>";

            helper.setText(correoHtml, true);

            mailSender.send(message);

            return "redirect:/principal?enviado";
        } catch (MessagingException e) {
            model.addAttribute("mensaje", "Error al enviar el correo.");
            e.printStackTrace();
        }
        return "enviar-correo";
    }
}
