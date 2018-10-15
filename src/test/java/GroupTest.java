import org.junit.Test;

import static org.junit.Assert.*;

public class GroupTest {

    @Test
    public void addStudent() {
        Student student = new Student(55, "Иванов Иван Иванович");
        Group group = new Group("Group1");
        group.addStudent(student);
        assertTrue(student == group.getStudents().get(0));
    }

    @Test
    public void removeStudent() {
        Student student = new Student(55, "Иванов Иван Иванович");
        Group group = new Group("Group1");
        group.addStudent(student);
        group.removeStudent(student);
        assertFalse(group.getStudents().contains(student));
    }

    @Test
    public void setHead() {
        Group group = createGroup();
        Student student = new Student(55, "Иванов Иван Иванович");
        group.addStudent(student);
        group.setHead(student);
        assertTrue(group.getHead() == student);

    }

    @Test
    public void searchStudentID() {
        Group group = createGroup();
        Student student = new Student(55, "Иванов Пётр Сидорович");
        group.addStudent(student);
        assertTrue(group.searchStudent(55) == student);
    }

    @Test
    public void searchStudentFIO() {
        Group group = createGroup();
        Student student = new Student(55, "Иванов Пётр Сидорович");
        group.addStudent(student);
        assertTrue(group.searchStudent("Иванов Пётр Сидорович") == student);
    }

    @Test
    public void getAverageMark() {
        Group group = createGroup();
        int mark = 3;
        for (Student student:group.getStudents()
             ) {
            student.addMark(mark);
            student.addMark(mark);
            student.addMark(mark);
            mark++;
        }
        assertEquals(4.0, group.getAverageMark(), 0.00001);
    }

    @Test
    public void getTitle() {
        Group group = createGroup();
        assertEquals(0, group.getTitle().compareTo("Group1"), 0.00001);
    }

    @Test
    public void getAmountStudents() {
        Group group = createGroup();
        assertEquals(3, group.getAmountStudents(), 0.00001);
    }

    private Group createGroup(){
        Student student1 = new Student(1, "Иванов Иван Иванович");
        Student student2 = new Student(2, "Петров Пётр Петрович");
        Student student3 = new Student(3, "Сидоров Сидр Сидорович");
        Group group = new Group("Group1");
        group.addStudent(student1);
        group.addStudent(student2);
        group.addStudent(student3);
        return group;

    }
}