package pl.krystian.lessonRegister.services;

import pl.krystian.lessonRegister.models.Lesson;
import pl.krystian.lessonRegister.models.Student;

import java.util.HashMap;
import java.util.List;

public interface ILessonService {
    void addNewLesson(Lesson lesson);
    int getAvailableId();
    void updateAvailableId();
    HashMap<Student,Boolean> getUncheckedAttendanceList();
    Lesson getLessonById(int id);
    List<Lesson> getLessonsList();
    void setLessonList(List<Lesson> lessons);
    void removeLesson(Lesson lesson);
}
