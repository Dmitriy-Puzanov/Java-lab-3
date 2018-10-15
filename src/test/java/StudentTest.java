import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {

    @Test
    public void getNum() {
        Student student = createStudent();
        assertEquals(3, student.getNum(), 0.00001);
    }

    @Test
    public void getAverageMark() {
        Student student = createStudent();
        assertEquals(4, student.getAverageMark(), 0.00001);
    }

    @Test
    public void addMark() {
        Student student = new Student(1, "Иванов Иван Иванович");
        student.addMark(3);
        assertEquals(1, student.getNum(), 0.00001);
    }

    @Test
    public void setGroup() {
        Student student = createStudent();
        Group group = new Group("test");
        student.setGroup(group);
        assertTrue(group == student.getGroup());
    }

    @Test
    public void getId() {
        Student student = createStudent();
        assertEquals(1, student.getId(), 0.00001);
    }

    @Test
    public void getFio() {
        Student student = createStudent();
        String fio = "Иванов Иван Иванович";
        assertEquals(0, fio.compareTo(student.getFio()));
    }

    @Test
    public void getGroup() {
        Student student = createStudent();
        Group group = new Group("test2");
        student.setGroup(group);
        assertTrue(group == student.getGroup());
    }

    private Student createStudent(){
        Student student = new Student(1, "Иванов Иван Иванович");
        student.addMark(3);
        student.addMark(4);
        student.addMark(5);
        return student;
    }
}