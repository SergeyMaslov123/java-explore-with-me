package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({StatClient.class})
@SpringBootApplication
public class EwnService {
    public static void main(String[] args) {
        SpringApplication.run(EwnService.class, args);
    }
}
