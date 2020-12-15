package pl.krystian.lessonRegister.view;

import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter {
    public void print(String text){
        System.out.println(text);
    }
}
