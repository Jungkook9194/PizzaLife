/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.error;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author henrysebastianoteroalvarez
 */
@Configuration
public class CustomErrorConfig400{
    @Bean
    public ErrorViewResolver customErrorViewResolver400() {
        return (request, status, model) -> {
            if (status == HttpStatus.BAD_REQUEST) {
                return new ModelAndView("error400", model);
            }
            return null;
        };
    }
}
