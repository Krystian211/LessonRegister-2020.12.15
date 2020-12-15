package pl.krystian.lessonRegister.view;

public enum MainMenuOption {
    EXIT(1,"Wyjdź z programu"),
    ADD_LESSON(2,"Dodaj nową lekcję"),
    CHECK_ATTENDANCE(3,"Sprawdź obecność"),
    ADD_STUDENT(4, "Dodaj studenta"),
    SHOW_STUDENT_LIST(5,"Pokaż listę studentów"),
    SAVE_DATA(6,"Zapisz dane"),
    SHOW_ATTENDANCE_LIST(7,"Wyświetl listę obecności"),
    SHOW_LESSONS_HISTORY(8,"Pokaż historię zajęć"),
    CHANGE_CURRENT_LESSON(9,"Zmień aktualne zajęcia"),
    REMOVE_STUDENT(10,"Usuń studenta"),
    REMOVE_LESSON(11,"Usuń zajęcia"),
    EDIT_LESSON(12,"Edytuj zajęcia"),
    EDIT_STUDENT(13,"Edytuj dane studenta");

    private int number;
    private String description;

    MainMenuOption(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public String toString() {
        return number+" - "+description;
    }

    public static String print(){
        StringBuilder stringBuilder=new StringBuilder();
        for (MainMenuOption value : values()) {
            stringBuilder.append(value.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    public static MainMenuOption toOption(int number){
        for (MainMenuOption value : values()) {
            if (value.number==number) {
                return value;
            }
        }
        return null;
    }
}
