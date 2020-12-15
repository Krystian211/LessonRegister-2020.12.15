package pl.krystian.lessonRegister.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.krystian.lessonRegister.database.ILessonRepository;
import pl.krystian.lessonRegister.database.IStudentRepository;
import pl.krystian.lessonRegister.models.Lesson;
import pl.krystian.lessonRegister.models.Student;
import pl.krystian.lessonRegister.services.IXMLService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XMLServiceImpl implements IXMLService {

    @Autowired
    ILessonRepository lessonRepository;

    @Autowired
    IStudentRepository studentRepository;

    private static final String STUDENTS_FILE_PATH = "./xmlFiles/Students.xml";
    private static final String LESSONS_FILE_PATH = "./xmlFiles/Lessons.xml";


    @Override
    public boolean writeStudents(List<Student> students) {
        boolean success = false;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("studentList");
            document.appendChild(root);

            Element studentElement;
            Element id;
            Element firstName;
            Element lastName;
            Element email;
            Element personalIdentityNumber;
            Element birthDate;

            for (Student student : studentRepository.getStudentList()) {
                studentElement = document.createElement("student");

                id=document.createElement("id");
                id.appendChild(document.createTextNode(Integer.toString(student.getId())));
                studentElement.appendChild(id);

                firstName=document.createElement("firstName");
                firstName.appendChild(document.createTextNode(student.getFirstName()));
                studentElement.appendChild(firstName);

                lastName=document.createElement("lastName");
                lastName.appendChild(document.createTextNode(student.getLastName()));
                studentElement.appendChild(lastName);

                email=document.createElement("email");
                email.appendChild(document.createTextNode(student.getEmail()));
                studentElement.appendChild(email);

                personalIdentityNumber=document.createElement("personalIdentityNumber");
                personalIdentityNumber.appendChild(document.createTextNode(student.getPersonalIdentityNumber()));
                studentElement.appendChild(personalIdentityNumber);

                birthDate=document.createElement("birthDate");
                birthDate.appendChild(document.createTextNode(student.getBirthDate().toString()));
                studentElement.appendChild(birthDate);

                root.appendChild(studentElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(STUDENTS_FILE_PATH));
            transformer.transform(domSource, streamResult);

            success = true;
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean writeLessons(List<Lesson> lessons) {
        boolean success = false;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("lessonsHistory");
            document.appendChild(root);

            Element lessonElement;
            Element subjectElement;
            Element lessonIdElement;
            Element dateElement;
            Element attendanceListElement;
            Element studentElement;
            Element attendanceElement;
            Attr studentIdAttribute;

            for (Lesson dbLesson : lessonRepository.getAllLessons()) {
                lessonElement=document.createElement("lesson");
                root.appendChild(lessonElement);

                lessonIdElement=document.createElement("id");
                lessonIdElement.appendChild(document.createTextNode(Integer.toString(dbLesson.getId())));
                lessonElement.appendChild(lessonIdElement);

                dateElement=document.createElement("date");
                dateElement.appendChild(document.createTextNode(dbLesson.getDate().toString()));
                lessonElement.appendChild(dateElement);

                subjectElement=document.createElement("subject");
                subjectElement.appendChild(document.createTextNode(dbLesson.getSubject()));
                lessonElement.appendChild(subjectElement);

                attendanceListElement=document.createElement("attendanceList");
                lessonElement.appendChild(attendanceListElement);

                for (Map.Entry<Student, Boolean> entry : dbLesson.getAttendanceList().entrySet()) {
                    studentElement=document.createElement("student");
                    studentIdAttribute=document.createAttribute("id");
                    studentIdAttribute.setValue(Integer.toString(entry.getKey().getId()));
                    studentElement.setAttributeNode(studentIdAttribute);

                    attendanceElement=document.createElement("attendance");
                    attendanceElement.appendChild(document.createTextNode(entry.getValue().toString()));
                    studentElement.appendChild(attendanceElement);

                    attendanceListElement.appendChild(studentElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(LESSONS_FILE_PATH));
            transformer.transform(domSource, streamResult);

            success = true;
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return success;
    }


    @Override
    public List<Student> readStudents() {
        if (!new File(STUDENTS_FILE_PATH).exists()) {
            return null;
        }
        List<Student> students=new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(STUDENTS_FILE_PATH));
            document.getDocumentElement().normalize();

            Element root=document.getDocumentElement();
            NodeList xmlStudents=root.getElementsByTagName("student");

            Element studentElement;
            Element id;
            Element firstName;
            Element lastName;
            Element email;
            Element personalIdentityNumber;
            String date;
            Element birthDate;
            for (int i = 0; i <xmlStudents.getLength() ; i++) {
                Student student=new Student();

                studentElement=(Element)xmlStudents.item(i);
                id=(Element)studentElement.getElementsByTagName("id").item(0);
                student.setId(Integer.parseInt(id.getTextContent()));

                firstName=(Element)studentElement.getElementsByTagName("firstName").item(0);
                student.setFirstName(firstName.getTextContent());

                lastName=(Element)studentElement.getElementsByTagName("lastName").item(0);
                student.setLastName(lastName.getTextContent());

                email=(Element)studentElement.getElementsByTagName("email").item(0);
                student.setEmail(email.getTextContent());

                personalIdentityNumber=(Element)studentElement.getElementsByTagName("personalIdentityNumber").item(0);
                student.setPersonalIdentityNumber(personalIdentityNumber.getTextContent());

                birthDate=(Element)studentElement.getElementsByTagName("birthDate").item(0);
                date=birthDate.getTextContent();
                DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
                student.setBirthDate(LocalDate.parse(date,dateTimeFormatter));

                students.add(student);
            }


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public List<Lesson> readLessons() {
        if (!new File(LESSONS_FILE_PATH).exists()) {
            return null;
        }
        List<Lesson> lessons=new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(LESSONS_FILE_PATH));
            document.getDocumentElement().normalize();

            Element root=document.getDocumentElement();

            NodeList lessonsNodeList;
            NodeList studentNodeList;
            Element lessonIdElement;
            Element lessonElement;
            Element subjectElement;
            Element dateElement;
            Element studentElement;
            Element attendanceListElement;
            Element attendanceElement;

            lessonsNodeList=root.getElementsByTagName("lesson");

            for (int i = 0; i <lessonsNodeList.getLength() ; i++) {
                Lesson lesson=new Lesson();
                lessonElement=(Element)lessonsNodeList.item(i);

                lessonIdElement=(Element)lessonElement.getElementsByTagName("id").item(0);
                lesson.setId(Integer.parseInt(lessonIdElement.getTextContent()));

                subjectElement=(Element)lessonElement.getElementsByTagName("subject").item(0);
                lesson.setSubject(subjectElement.getTextContent());

                dateElement=(Element)lessonElement.getElementsByTagName("date").item(0);
                DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
                lesson.setDate(LocalDate.parse(dateElement.getTextContent(),dateTimeFormatter));

                attendanceListElement=(Element)lessonElement.getElementsByTagName("attendanceList").item(0);
                studentNodeList=attendanceListElement.getElementsByTagName("student");
                Map<Student,Boolean> attendanceList=new HashMap<>();
                for (int j = 0; j <studentNodeList.getLength() ; j++) {
                    studentElement=(Element)studentNodeList.item(j);
                    attendanceElement=(Element) studentElement.getElementsByTagName("attendance").item(0);
                    attendanceList.put(studentRepository.getStudentById(Integer.parseInt(studentElement.getAttribute("id"))),
                            Boolean.valueOf(attendanceElement.getTextContent()));
                }
                lesson.setAttendanceList(attendanceList);
                lessons.add(lesson);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return lessons;
    }


}
