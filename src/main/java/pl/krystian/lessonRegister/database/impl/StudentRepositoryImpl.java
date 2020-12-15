package pl.krystian.lessonRegister.database.impl;

import org.springframework.stereotype.Repository;
import pl.krystian.lessonRegister.database.IStudentRepository;
import pl.krystian.lessonRegister.models.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class StudentRepositoryImpl implements IStudentRepository {
    private int availableId=1;
    List<Student> students=new ArrayList<>();

    @Override
    public void addStudent(Student student) {
        students.add(student);
        Collections.sort(students);
    }

    @Override
    public List<Student> getStudentList() {
        return students;
    }

    @Override
    public int pollAvailableId() {
        int id=availableId;
        availableId+=1;
        return id;
    }

    @Override
    public int getAvailableId() {
        return this.availableId;
    }

    @Override
    public void setAvailableId(int availableId) {
        this.availableId=availableId;
    }


    @Override
    public void setStudentList(List<Student> students) {
        this.students=students;
    }

    @Override
    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId()==id) {
                return student;
            }
        }
        return null;
    }

    @Override
    public void removeStudent(Student student) {
        students.remove(student);
    }

}
