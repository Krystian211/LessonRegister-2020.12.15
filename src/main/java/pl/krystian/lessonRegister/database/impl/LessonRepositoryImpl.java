package pl.krystian.lessonRegister.database.impl;

import org.springframework.stereotype.Repository;
import pl.krystian.lessonRegister.database.ILessonRepository;
import pl.krystian.lessonRegister.models.Lesson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class LessonRepositoryImpl  implements ILessonRepository {
    private int availableId=1;
    List<Lesson> lessons=new ArrayList<>();

    @Override
    public int getAvailableId() {
        int id=availableId;
        availableId+=1;
        return id;
    }

    @Override
    public void addNewLesson(Lesson lesson) {
        lessons.add(lesson);
        Collections.sort(lessons);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessons;
    }

    @Override
    public void setLessonList(List<Lesson> lessons) {
        this.lessons=lessons;
    }

    @Override
    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    @Override
    public Lesson getLessonById(int id) {
        return null;
    }

    @Override
    public void setAvailableId(int id) {
        this.availableId=id;
    }

}
