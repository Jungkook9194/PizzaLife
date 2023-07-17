/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.error;

import org.jboss.resteasy.spi.InternalServerErrorException;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@ControllerAdvice
public class CustomErrorConfig500{
    @ExceptionHandler(Exception.class)
    public String Error500() {
        return "error500";
    }
}