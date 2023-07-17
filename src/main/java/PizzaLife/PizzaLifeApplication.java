package PizzaLife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class PizzaLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaLifeApplication.class, args);
    }

    @Bean
    public ErrorViewResolver customErrorViewResolver() {
        return (request, status, model) -> {
            if (status == HttpStatus.NOT_FOUND) {
                return new ModelAndView("error404");
            }
            return null;
        };
    }
}
