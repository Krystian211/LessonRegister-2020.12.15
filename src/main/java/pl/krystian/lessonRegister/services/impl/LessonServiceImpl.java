package pl.krystian.lessonRegister.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.krystian.lessonRegister.database.ILessonRepository;
import pl.krystian.lessonRegister.database.IStudentRepository;
import pl.krystian.lessonRegister.models.Lesson;
import pl.krystian.lessonRegister.models.Student;
import pl.krystian.lessonRegister.services.ILessonService;

import java.util.HashMap;
import java.util.List;

@Service
public class LessonServiceImpl implements ILessonService {
    @Autowired
    ILessonRepository lessonRepository;

    @Autowired
    IStudentRepository studentRepository;

    @Override
    public void addNewLesson(Lesson lesson) {
        lessonRepository.addNewLesson(lesson);
    }

    @Override
    public int getAvailableId() {
        return lessonRepository.getAvailableId();
    }

    @Override
    public void updateAvailableId() {
        int id=1;
        for (Lesson lesson : lessonRepository.getAllLessons()) {
            if (lesson.getId()>id) {
                id=lesson.getId();
            }
        }
        lessonRepository.setAvailableId(id);
    }

    @Override
    public HashMap<Student, Boolean> getUncheckedAttendanceList() {
        HashMap<Student,Boolean> attendanceList=new HashMap<>();
        for (Student student : studentRepository.getStudentList()) {
            attendanceList.put(student,false);
        }
        return attendanceList;
    }

    @Override
    public Lesson getLessonById(int id) {
        for (Lesson lesson : lessonRepository.getAllLessons()) {
            if (lesson.getId()==id) {
                return lesson;
            }
        }
        return null;
    }

    @Override
    public List<Lesson> getLessonsList() {
        return lessonRepository.getAllLessons();
    }

    @Override
    public void setLessonList(List<Lesson> lessons) {
        lessonRepository.setLessonList(lessons);
    }

    @Override
    public void removeLesson(Lesson lesson) {
        lessonRepository.removeLesson(lesson);
    }
}
