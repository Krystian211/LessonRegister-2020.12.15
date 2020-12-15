package pl.krystian.lessonRegister.controllers;

import io.bretty.console.table.Alignment;
import io.bretty.console.table.ColumnFormatter;
import io.bretty.console.table.Precision;
import io.bretty.console.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.krystian.lessonRegister.models.Lesson;
import pl.krystian.lessonRegister.models.Student;
import pl.krystian.lessonRegister.services.ILessonService;
import pl.krystian.lessonRegister.services.IStudentService;
import pl.krystian.lessonRegister.services.IXMLService;
import pl.krystian.lessonRegister.view.ConsolePrinter;
import pl.krystian.lessonRegister.view.MainMenuOption;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Controller
public class MainController {

    int currentLessonId = 0;

    @Autowired
    Scanner scanner;

    @Autowired
    ConsolePrinter consolePrinter;

    @Autowired
    ILessonService lessonService;

    @Autowired
    IStudentService studentService;

    @Autowired
    IXMLService xmlService;

    public void runMainLoop() {
        boolean exit = false;
        initialize();
        while (!exit) {
            consolePrinter.print("---Dziennik obecnośći---");
            consolePrinter.print(MainMenuOption.print());
            switch (getOptionFromUser()) {
                case EXIT:
                    closeApp();
                    exit = true;
                    break;
                case ADD_LESSON:
                    addNewLesson();
                    break;

                case CHECK_ATTENDANCE:
                    checkAttendance();
                    break;

                case ADD_STUDENT:
                    addStudent();
                    break;

                case SHOW_STUDENT_LIST:
                    showStudentList();
                    break;

                case SAVE_DATA:
                    xmlService.writeStudents(studentService.getStudentList());
                    xmlService.writeLessons(lessonService.getLessonsList());
                    break;

                case SHOW_ATTENDANCE_LIST:
                    consolePrinter.print(createAttendanceList());
                    break;

                case SHOW_LESSONS_HISTORY:
                    consolePrinter.print(createLessonHistory());
                    break;

                case REMOVE_LESSON:
                    removeLesson();
                    break;

                case REMOVE_STUDENT:
                    removeStudent();
                    break;

                case EDIT_LESSON:
                    break;

                case EDIT_STUDENT:
                    break;

                case CHANGE_CURRENT_LESSON:
                    changeCurrentLesson();
                    break;
            }
        }
    }

    private void changeCurrentLesson(){
        consolePrinter.print("---Zmiana zajęć---");
        consolePrinter.print(createLessonHistory());
        Integer lessonId;
        if ((lessonId=getLessonId())==null) {
            return;
        }else {
            currentLessonId=lessonId;
            consolePrinter.print("Zmieniono zajęcia");
        }
    }

    private void removeLesson(){
        consolePrinter.print("---Usuwanie zajęć---");
        consolePrinter.print(createLessonHistory());
        Integer lessonId;
        if ((lessonId=getLessonId())==null) {
            return;
        }else {
            lessonService.removeLesson(lessonService.getLessonById(lessonId));
            consolePrinter.print("Usunięto zajęcia.");
        }
    }

    private void removeStudent(){
        consolePrinter.print("---Usuwanie studenta---");
        consolePrinter.print(createStudentTable());
        Integer studentId;
        if ((studentId=getStudentId())==null) {
            return;
        }else {
            studentService.removeStudent(studentService.getStudentById(studentId));
            consolePrinter.print("Usunięto studenta.");
        }
    }

    private String createLessonHistory() {
        if (lessonService.getLessonsList().size() == 0) {
            return "Brak danych do wyświetlenia";
        } else {
            int actualRow = 0;
            int rows = lessonService.getLessonsList().size();
            Integer[] ids = new Integer[rows];
            String[] dates = new String[rows];
            String[] subjects = new String[rows];

            for (Lesson lesson : lessonService.getLessonsList()) {
                ids[actualRow]=lesson.getId();
                dates[actualRow]=lesson.getDate().toString();
                subjects[actualRow]=lesson.getSubject();
                actualRow++;
            }

            ColumnFormatter<Number> idFormatter = ColumnFormatter.number(Alignment.LEFT, 6, Precision.ZERO);
            ColumnFormatter<String> datesFormatter = ColumnFormatter.text(Alignment.LEFT, 10);
            ColumnFormatter<String> subjectFormatter = ColumnFormatter.text(Alignment.LEFT, 50);

            Table.Builder builder = new Table.Builder("ID", ids, idFormatter);
            builder.addColumn("Data", dates, datesFormatter);
            builder.addColumn("Temat", subjects, subjectFormatter);

            Table table = builder.build();
            return table.toString();
        }
    }

    private void initialize() {
        List<Student> students;
        if ((students = xmlService.readStudents()) != null) {
            studentService.setStudentList(students);
        }
        List<Lesson> lessons;
        if ((lessons = xmlService.readLessons()) != null) {
            lessonService.setLessonList(lessons);
        }
        studentService.updateAvailableId();
        lessonService.updateAvailableId();
    }

    private void closeApp() {
        scanner.close();
        consolePrinter.print("Zamykanie aplikacji");
        System.exit(0);
    }

    private MainMenuOption getOptionFromUser() {
        boolean correct = false;
        MainMenuOption option = null;
        do {
            try {
                if ((option = MainMenuOption.toOption(Integer.parseInt(scanner.nextLine()))) == null) {
                    consolePrinter.print("Wybrano opcję spoza zakresu!");
                } else {
                    correct = true;
                }
            } catch (NumberFormatException e) {
                consolePrinter.print("Podano literę zamiast liczby!");
            }
        } while (!correct);
        return option;
    }

    private void addNewLesson() {
        String subject;
        consolePrinter.print("---Dodawanie nowej lekcji---");
        if ((subject = getSubject()) == null) {
            return;
        }
        int lessonId = lessonService.getAvailableId();
        lessonService.addNewLesson(new Lesson(lessonId, subject, LocalDate.now(), lessonService.getUncheckedAttendanceList()));
        consolePrinter.print("Dodano nową lekcję");
        currentLessonId = lessonId;
    }

    private void checkAttendance() {
        consolePrinter.print("---Sprawdzanie obecności---");
        if (currentLessonId==0) {
            consolePrinter.print("Brak danych do wyświetlenia");
            return;
        }
        for (Map.Entry<Student, Boolean> entry : lessonService.getLessonById(currentLessonId).getAttendanceList().entrySet()) {
            String attendance;
            boolean correct = false;
            do {
                consolePrinter.print(entry.getKey().getFirstName() + " " + entry.getKey().getLastName() + " obecny/na? t/n");
                attendance = scanner.nextLine();
                if (attendance.equals("t")) {
                    consolePrinter.print("obecny/na");
                    entry.setValue(true);
                    correct = true;
                } else if (attendance.equals("n")) {
                    consolePrinter.print("nieobecny/na");
                    entry.setValue(false);
                    correct = true;
                } else {
                    consolePrinter.print("Podano nieznany znak!");
                }

            } while (!correct);
        }
        consolePrinter.print("---Obecność sprawdzona---");
    }

    void addStudent() {
        consolePrinter.print("---Dodawanie studenta---");
        String firstName;
        if ((firstName = getFirstName()) == null) {
            return;
        }
        String lastName;
        if ((lastName = getLastName()) == null) {
            return;
        }
        String email;
        if ((email = getEmail()) == null) {
            return;
        }
        String personalIdentityNumber;
        if ((personalIdentityNumber = getPersonalIdentityNumber()) == null) {
            return;
        }
        LocalDate birthDate;
        if ((birthDate = getBirthDate()) == null) {
            return;
        }
        Student student = new Student(studentService.pollAvailableId(), firstName, lastName, email, personalIdentityNumber, birthDate);

        if (studentService.areStudentDataAvailable(student)) {
            studentService.addStudent(student);
            consolePrinter.print("Dodano studenta");
        } else {
            consolePrinter.print("Student o podanych danych już istnieje");
        }
    }

    private String getAndValidateString(String initialMessage, String failMessage, String regex) {
        String string = null;
        boolean dataCorrect = false;
        while (!dataCorrect) {
            consolePrinter.print(initialMessage);
            consolePrinter.print("\"exit\" aby anulować");
            string = scanner.nextLine();
            if (string.equalsIgnoreCase("exit")) {
                dataCorrect = true;
                string = null;
                continue;
            }
            if (string.matches(regex)) {
                dataCorrect = true;
            } else {
                consolePrinter.print(failMessage);
            }
        }
        return string;
    }

    public Integer getLessonId(){
        boolean dataCorrect=false;
        Integer id=null;
        while (!dataCorrect) {
            consolePrinter.print("Podaj numer Id zajęć.");
            consolePrinter.print("\"exit\" aby anulować");
            String string=scanner.nextLine();
            if (string.equalsIgnoreCase("exit")) {
                dataCorrect=true;
            }else {
                try {
                    id=Integer.parseInt(string);
                    if (lessonService.getLessonById(id)==null) {
                        consolePrinter.print("Brak lekcji o podanym Id");
                    }else {
                        dataCorrect=true;
                    }
                } catch (NumberFormatException e) {
                    consolePrinter.print("Niepoprawny Id!");
                }
            }
        }
        return id;
    }

    public Integer getStudentId(){
        boolean dataCorrect=false;
        Integer id=null;
        while (!dataCorrect) {
            consolePrinter.print("Podaj numer Id studenta.");
            consolePrinter.print("\"exit\" aby anulować");
            String string=scanner.nextLine();
            if (string.equalsIgnoreCase("exit")) {
                dataCorrect=true;
            }else {
                try {
                    id=Integer.parseInt(string);
                    if (studentService.getStudentById(id)==null) {
                        consolePrinter.print("Brak studenta o podanym Id");
                    }else {
                        dataCorrect=true;
                    }
                } catch (NumberFormatException e) {
                    consolePrinter.print("Niepoprawny Id!");
                }
            }
        }
        return id;
    }

    String getFirstName() {
        return getAndValidateString("Podaj imię.", "Niepoprawny format imienia!", "[A-ZĄĘĆŻŹŁÓŃŚ][a-ząęćżźłóńś]+");
    }

    String getLastName() {
        return getAndValidateString("Podaj nazwisko.", "Niepoprawny format nazwiska!", "[A-ZĄĘĆŻŹŁÓŃŚ][a-ząęćżźłóńś]+");
    }

    String getEmail() {
        return getAndValidateString("Podaj email.", "Niepoprawny format emaila!", "[a-zA-Z0-9]+@[a-zA-Z0-9]{2,}.[a-zA-Z]{2,3}");
    }

    String getPersonalIdentityNumber() {
        return getAndValidateString("Podaj pesel.", "Niepoprawny format numeru pesel!", "[0-9]{11}");
    }

    LocalDate getBirthDate() {
        String stringDate;
        LocalDate date = null;
        boolean dataCorrect = false;

        while (!dataCorrect) {
            consolePrinter.print("Podaj datę urodzenia w formacie yyyy-MM-dd");
            stringDate = scanner.nextLine();
            if (stringDate.equalsIgnoreCase("exit")) {
                dataCorrect = true;
                continue;
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                date = LocalDate.parse(stringDate, dateTimeFormatter);
                dataCorrect = true;
            } catch (Exception e) {
                consolePrinter.print("Niepoprawny foramt daty");
            }
        }
        return date;
    }

    String getSubject() {
        return getAndValidateString("Podaj temat.", "Niepoprawny format tamatu!", "[A-ZĄĘĆŻŹŁÓŃŚ][a-ząęćżźłóńś]+");
    }

    void showStudentList() {
        consolePrinter.print("---Lista studentów---");
        consolePrinter.print(createStudentTable());
    }

    private String createStudentTable() {
        if (studentService.getStudentList().size() == 0) {
            return "Brak danych do wyświetlenia";
        } else {
            int actualRow = 0;
            int rows = studentService.getStudentList().size();
            Integer[] ids = new Integer[rows];
            String[] firstNames = new String[rows];
            String[] lastNames = new String[rows];
            String[] email = new String[rows];
            String[] personalIdentityNumbers = new String[rows];

            for (Student student : studentService.getStudentList()) {
                ids[actualRow] = student.getId();
                firstNames[actualRow] = student.getFirstName();
                lastNames[actualRow] = student.getLastName();
                email[actualRow] = student.getEmail();
                personalIdentityNumbers[actualRow] = student.getPersonalIdentityNumber();
                actualRow++;

            }
            ColumnFormatter<Number> idFormatter = ColumnFormatter.number(Alignment.LEFT, 6, Precision.ZERO);
            ColumnFormatter<String> firstNameFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
            ColumnFormatter<String> lastNameFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
            ColumnFormatter<String> emailFormatter = ColumnFormatter.text(Alignment.LEFT, 30);
            ColumnFormatter<String> personalIdentityNumberFormatter = ColumnFormatter.text(Alignment.CENTER, 13);

            Table.Builder builder = new Table.Builder("ID", ids, idFormatter);
            builder.addColumn("Imię", firstNames, firstNameFormatter);
            builder.addColumn("Nazwisko", lastNames, lastNameFormatter);
            builder.addColumn("Email", email, emailFormatter);
            builder.addColumn("PESEL", personalIdentityNumbers, personalIdentityNumberFormatter);
            Table table = builder.build();
            return table.toString();
        }
    }

    private String createAttendanceList() {
        if (currentLessonId == 0) {
            return "Brak danych do wyświetlenia";
        } else {
            Lesson lesson = lessonService.getLessonById(currentLessonId);

            int actualRow = 0;
            int rows = studentService.getStudentList().size();
            Integer[] ids = new Integer[rows];
            String[] firstNames = new String[rows];
            String[] lastNames = new String[rows];
            String[] attendances = new String[rows];

            for (Map.Entry<Student, Boolean> entry : lesson.getAttendanceList().entrySet()) {
                ids[actualRow] = entry.getKey().getId();
                firstNames[actualRow] = entry.getKey().getFirstName();
                lastNames[actualRow] = entry.getKey().getLastName();
                if (entry.getValue()) {
                    attendances[actualRow] = "Tak";
                } else {
                    attendances[actualRow] = "Nie";
                }
                actualRow++;
            }

            ColumnFormatter<Number> idFormatter = ColumnFormatter.number(Alignment.LEFT, 6, Precision.ZERO);
            ColumnFormatter<String> firstNameFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
            ColumnFormatter<String> lastNameFormatter = ColumnFormatter.text(Alignment.LEFT, 25);
            ColumnFormatter<String> attendanceFormatter = ColumnFormatter.text(Alignment.LEFT, 10);


            Table.Builder builder = new Table.Builder("ID", ids, idFormatter);
            builder.addColumn("Imię", firstNames, firstNameFormatter);
            builder.addColumn("Nazwisko", lastNames, lastNameFormatter);
            builder.addColumn("Obecność", attendances, attendanceFormatter);
            Table table = builder.build();
            return table.toString();
        }
    }
}
