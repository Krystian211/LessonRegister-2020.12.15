package pl.krystian.lessonRegister.database;

import pl.krystian.lessonRegister.models.Student;

import java.util.List;

public interface IStudentRepository {
    void addStudent(Student student);
    List<Student> getStudentList();
    int pollAvailableId();
    int getAvailableId();
    void setAvailableId(int availableId);
    void setStudentList(List<Student> students);
    Student getStudentById(int id);
    void removeStudent(Student student);
}
