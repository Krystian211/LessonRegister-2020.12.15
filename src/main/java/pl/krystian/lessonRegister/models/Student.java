package pl.krystian.lessonRegister.models;

import java.time.LocalDate;
import java.util.Objects;

public class Student implements Comparable<Student>{
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String personalIdentityNumber;
    private LocalDate birthDate;

    public Student(int id, String firstName, String lastName, String email, String personalIdentityNumber, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalIdentityNumber = personalIdentityNumber;
        this.birthDate = birthDate;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalIdentityNumber() {
        return personalIdentityNumber;
    }

    public void setPersonalIdentityNumber(String personalIdentityNumber) {
        this.personalIdentityNumber = personalIdentityNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean equalsWithoutId(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                Objects.equals(firstName, student.firstName) &&
                Objects.equals(lastName, student.lastName) &&
                Objects.equals(email, student.email) &&
                Objects.equals(personalIdentityNumber, student.personalIdentityNumber) &&
                Objects.equals(birthDate, student.birthDate);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                Objects.equals(firstName, student.firstName) &&
                Objects.equals(lastName, student.lastName) &&
                Objects.equals(email, student.email) &&
                Objects.equals(personalIdentityNumber, student.personalIdentityNumber) &&
                Objects.equals(birthDate, student.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, personalIdentityNumber, birthDate);
    }

    @Override
    public int compareTo(Student o) {
        int result;
        if ((result=this.firstName.compareToIgnoreCase(o.firstName))==0) {
            return this.lastName.compareToIgnoreCase(o.lastName);
        }else {
            return result;
        }
    }

    @Override
    public String toString() {
        return  "id=" + id +" "+firstName+" "+lastName+" "+email+" "+personalIdentityNumber;
    }
}
