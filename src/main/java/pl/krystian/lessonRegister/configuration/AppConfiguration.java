package pl.krystian.lessonRegister.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan("pl.krystian.lessonRegister")
public class AppConfiguration {
    @Bean
    Scanner scanner(){
        return new Scanner(System.in);
    }
}
