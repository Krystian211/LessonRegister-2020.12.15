package pl.krystian.lessonRegister.services;

import pl.krystian.lessonRegister.models.Lesson;
import pl.krystian.lessonRegister.models.Student;

import java.util.List;

public interface IXMLService {
    boolean writeStudents(List<Student> students);
    boolean writeLessons(List<Lesson>lessons);
    List<Student> readStudents();
    List<Lesson> readLessons();
}
