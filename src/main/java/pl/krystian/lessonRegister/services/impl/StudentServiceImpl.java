package pl.krystian.lessonRegister.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krystian.lessonRegister.database.ILessonRepository;
import pl.krystian.lessonRegister.database.IStudentRepository;
import pl.krystian.lessonRegister.models.Student;
import pl.krystian.lessonRegister.services.IStudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    ILessonRepository lessonRepository;

    @Autowired
    IStudentRepository studentRepository;

    @Override
    public int pollAvailableId() {
        return studentRepository.pollAvailableId();
    }

    @Override
    public int getAvailableId() {
        return studentRepository.getAvailableId();
    }

    @Override
    public void updateAvailableId() {
        int id=0;
        for (Student student : studentRepository.getStudentList()) {
            if (student.getId()>id) {
                id=student.getId();
            }
        }
        studentRepository.setAvailableId(id+1);
    }

    @Override
    public void addStudent(Student student) {
        studentRepository.addStudent(student);
    }

    @Override
    public boolean areStudentDataAvailable(Student studentData) {
        for (Student student : studentRepository.getStudentList()) {
            if (student.equalsWithoutId(studentData)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Student> getStudentList() {
        return studentRepository.getStudentList();
    }

    @Override
    public void setStudentList(List<Student> students) {
        studentRepository.setStudentList(students);
    }

    @Override
    public void removeStudent(Student student) {
        studentRepository.removeStudent(student);
    }

    @Override
    public Student getStudentById(int id) {
        return studentRepository.getStudentById(id);
    }

}
