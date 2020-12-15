package pl.krystian.lessonRegister.database;

import pl.krystian.lessonRegister.models.Lesson;

import java.util.List;

public interface ILessonRepository {
    int getAvailableId();
    void addNewLesson(Lesson lesson);
    List<Lesson> getAllLessons();
    void setLessonList(List<Lesson> lessons);
    void removeLesson(Lesson lesson);
    Lesson getLessonById(int id);
    void setAvailableId(int id);

}
