package ipsen2.groep8.werkplekkenreserveringsappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WerkplekkenReserveringsAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WerkplekkenReserveringsAppBackendApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200", "http://localhost:8080").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE").allowCredentials(true);
            }
        };
    }

}
