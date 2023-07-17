/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.controller;

import PizzaLife.emailservice.EmailService;
import PizzaLife.entity.gestion.UsuarioEntity;
import PizzaLife.entity.gestion.VerificationCodeGenerator;
import PizzaLife.repository.UsuarioRepository;
import PizzaLife.seguridad.Seguridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String submitForgotPasswordForm(@RequestParam("correo") String correo, Model model) {
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            String codigoVerificacion = VerificationCodeGenerator.generateCode(6);
            usuario.setCodereset(codigoVerificacion);
            usuarioRepository.save(usuario);
            emailService.sendVerificationCode(correo, codigoVerificacion);
            model.addAttribute("correo", correo);
            return "verify-code";
        } else {
            return "redirect:/forgot-password?correono";
        }
    }

    @PostMapping("/resend-code")
    public String resendVerificationCode(@RequestParam("correo") String correo, Model model) {
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            String codigoVerificacion = VerificationCodeGenerator.generateCode(6);
            usuario.setCodereset(codigoVerificacion);
            usuarioRepository.save(usuario);
            emailService.sendVerificationCode(correo, codigoVerificacion);
            model.addAttribute("correo", correo);
            return "verify-code";
        } else {
            return "redirect:/forgot-password?correono";
        }
    }

    @GetMapping("/verify-code")
    public String showVerifyCodeForm(Model model, String correo) {
        return "verify-code";
    }

    @PostMapping("/verify-code")
    public String submitVerifyCodeForm(@RequestParam("correo") String correo,
            @RequestParam("codigo") String codigo,
            Model model) {
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null && codigo.equals(usuario.getCodereset())) {
            model.addAttribute("correo", correo);
            model.addAttribute("codigo", codigo);
            return "reset-password";
        } else {
            model.addAttribute("correo", correo);
            return "redirect:/verify-code?nocode";

        }
    }

    @PostMapping("/reset-password")
    public String submitResetPasswordForm(@RequestParam("correo") String correo,
            @RequestParam("password") String password,
            Model model) {
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            String passwordEncriptada = passwordEncoder.encode(password);
            usuario.setPassword(passwordEncriptada);
            usuarioRepository.save(usuario);
            return "redirect:/login?passwordResetSuccess";
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "reset-password";
        }
    }
}
