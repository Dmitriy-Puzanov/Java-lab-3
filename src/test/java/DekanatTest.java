import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;


public class DekanatTest {

    @Test
    public void addMarks() {

            Student student = new Student("Лежанкин М.И.",1,1,"test" );
            student.Marks.add(4);
            student.Marks.add(3);
            assertEquals(2, student.Marks.size());
    }

    @Test
    public void calcStudMediumMark() {
        double sum=0;
        Student student = new Student("Лежанкин М.И.",1,1,"test" );
        student.Marks.add(4);
        student.Marks.add(3);
        student.Marks.add(2);
        student.Marks.add(5);
        student.Marks.add(4);
        for (int k:student.Marks)
            sum += k;
        student.MediumMark = sum/student.Marks.size();
        assertEquals(3.6, student.MediumMark,0.01);
    }
    @Test
    public void addStudent() {
        ArrayList<Group> groups = new ArrayList<>();
        Group group = new Group("test",1);
        groups.add(group);
        Student student = new Student("Лежанкин М.И.",1,1,"test" );
        group = groups.stream().filter(g -> g.TitleGroup.equals(student.TitleGroup)).findFirst().orElse(null);
        group.student.add(student);
        assertEquals(1, group.student.size());
    }
    @Test
    public void initHide(){
        Student stud;
        Random r = new Random();
  //      ArrayList<Group> groups = new ArrayList<>();
        Group group = new Group("test",0);
 //       groups.add(group);
        Student student = new Student("Лежанкин М.И.",1,1,"test" );
        Student student1 = new Student("Журавлева М.М.",2,1,"test" );
        group.student.add(student);
        group.student.add(student1);
        stud =   group.student.get(r.nextInt(group.student.size()));
        group.Hide = stud.IDStudent;
        boolean b = (group.Hide!=0);
        assertEquals(true, b);
        }

    @Test
    public void calcGroupMediumMark() {
        Student stud;
        double sum=0;
        Group group = new Group("test",0);
        Student student = new Student("Лежанкин М.И.",1,1,"test" );
        Student student1 = new Student("Журавлева М.М.",2,1,"test" );
        Student student2 = new Student("Иванова Л.Е.",3,1,"test" );
        student.MediumMark = 3.4;student1.MediumMark=2.8;student2.MediumMark=4.5;
        group.student.add(student); group.student.add(student1); group.student.add(student2);
        for (Student k:group.student) {
            stud = k;
            sum += stud.MediumMark;
        }
        group.GroupMediummark = sum/group.student.size();
        assertEquals(3.56, group.GroupMediummark,0.01);
    }

    @Test
    public void removeStudent(){
        Student stud;
        Group group = new Group("test",0);
        Student student = new Student("Лежанкин М.И.",1,1,"test" );
        Student student1 = new Student("Журавлева М.М.",2,1,"test" );
        Student student2 = new Student("Иванова Л.Е.",3,1,"test" );
        student.MediumMark = 3.4;student1.MediumMark=2.8;student2.MediumMark=4.5;
        group.student.add(student); group.student.add(student1); group.student.add(student2);
        for (Student k:group.student) {
            stud = k;
            if (stud.MediumMark < 2.9)
                group.student.remove(k);
            }
        assertEquals(2, group.student.size());
    }

    @Test
    public void searchStudent() {
        String fio = "Иванова Л.Е.";
        Student s=searchFIO(fio);

        assertEquals("Иванова Л.Е.", s.FIO);
    }


    private Student searchFIO(String fio) {
        Student stud;

        Group group = new Group("test", 0);
        Student student = new Student("Лежанкин М.И.", 1, 1, "test");
        Student student1 = new Student("Журавлева М.М.", 2, 1, "test");
        Student student2 = new Student("Иванова Л.Е.", 3, 1, "test");
        group.student.add(student);
        group.student.add(student1);
        group.student.add(student2);
        for (Student k : group.student) {
            stud = k;
            if (fio.equals((stud.FIO)))
            return stud;
        }
        return null;
    }
}
