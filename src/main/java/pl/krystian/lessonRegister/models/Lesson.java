package pl.krystian.lessonRegister.models;

import java.time.LocalDate;
import java.util.Map;

public class Lesson implements Comparable<Lesson>{
    private int id;
    private String subject;
    private LocalDate date;
    private Map<Student,Boolean> attendanceList;

    public Lesson(int id, String subject, LocalDate date, Map<Student, Boolean> attendanceList) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.attendanceList = attendanceList;
    }

    public Lesson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<Student, Boolean> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(Map<Student, Boolean> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @Override
    public int compareTo(Lesson o) {
     return date.compareTo(o.date);
    }
}
