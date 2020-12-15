package pl.krystian.lessonRegister.services;

import pl.krystian.lessonRegister.models.Student;

import java.util.List;

public interface IStudentService {
    int pollAvailableId();
    int getAvailableId();
    void updateAvailableId();
    void addStudent(Student student);
    boolean areStudentDataAvailable(Student studentData);
    List<Student> getStudentList();
    void setStudentList(List<Student> students);
    void removeStudent(Student student);
    Student getStudentById(int id);
}
