package pl.krystian.lessonRegister;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.krystian.lessonRegister.configuration.AppConfiguration;
import pl.krystian.lessonRegister.controllers.MainController;

public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext(AppConfiguration.class);
        MainController mainController=applicationContext.getBean(MainController.class);
        mainController.runMainLoop();
    }
}
