/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PizzaLife.error;

import io.undertow.util.BadRequestException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorControler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return "error405";
    }
}
