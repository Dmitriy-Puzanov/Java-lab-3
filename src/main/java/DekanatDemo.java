import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DekanatDemo {
    public static void main(String[] args) {
        Dekanat D1 = new Dekanat();
        D1.Download();
        D1.SelectHead();
        D1.Grading(D1.Students);
        D1.PrinStudents();
        D1.Unloading();
    }
}

class Student {

    int ID;
    String Fio;
    Group Group;
    ArrayList<Integer> Marks = new ArrayList<Integer>();
    int Num;

    Student(int id, String fio) {
        ID = id;
        Fio = fio;
    }

    void StudentGroup(Group group) {
        Group = group;
    }

    void StudentMarks(int marks) {
        Marks.add(marks);
        Num++;
    }

    int MarksAverage() {

        if (Num == 0){
            return 0;
        }

        int sum = 0;
        for (int str : Marks) {
            sum += str;
        }
        return sum / Num;
    }
}

class Group {
    String Title;
    Map<Integer, Student> Students = new HashMap<Integer, Student>();
    int Num;
    Student Head;

    Group(String title) {
        Title = title;
    }

    void AddStudentGroup(Student student) {
        Students.put(student.ID, student);
        student.Group = this;
    }

    void SelectHead(Student student) {
        Head = student;
    }

    Student SearchStudent(int num) {
        return Students.get(num);
    }

    int AverageMark() {
        int sum = 0;

        for (Student str : Students.values()) {
            sum += str.MarksAverage();
        }

        return sum;
    }

    void DeleteStudentGroup(Student student) {
        Students.remove(student);
        student.StudentGroup(null);
    }
}

class Dekanat {

    ArrayList<Student> Students = new ArrayList<Student>();
    Map<String, Group> Groups = new HashMap<String, Group>();

    Dekanat() {
    }

    void Download() {

        try {
            File f = new File("DownloadDekanat.json");
            JSONParser parser = new JSONParser();
            FileReader fr = new FileReader(f);
            Object obj = parser.parse(fr);
            JSONObject js = (JSONObject) obj;

            JSONArray students = (JSONArray) js.get("students");
            for (Object i : students) {

                String mGroup = ((JSONObject) i).get("groups").toString();
                String mStudent = ((JSONObject) i).get("fio").toString();
                int mID = Integer.parseInt(((JSONObject) i).get("id").toString());

                Group MyGroup = Groups.get(mGroup);

                if (MyGroup == null) {
                    MyGroup = new Group(mGroup);
                    Groups.put(mGroup, MyGroup);
                }

                Student MyStudent = new Student(mID, mStudent);
                Students.add(MyStudent);

                MyGroup.AddStudentGroup(MyStudent);

            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void Grading(ArrayList<Student> Students) {

        int min = 1;
        int max = 5;
        Random rnd = new Random(System.currentTimeMillis());

        for (Student mStudent : Students) {
            mStudent.StudentMarks(min + rnd.nextInt(max - min + 1));
        }

    }

    void StudentTransfer(ArrayList<Student> Students, Group NewGroup) {

        for (Student mStudent : Students) {
            mStudent.StudentGroup(NewGroup);
        }
    }

    void ExpulsionOfStudents(int MinMark) {

        for (Student mStudent : Students) {
            if (mStudent.MarksAverage() < MinMark) {
                if (mStudent.Group != null) {
                    mStudent.Group.DeleteStudentGroup(mStudent);
                }
            }
        }
    }

    void SelectHead() {

        Random rnd = new Random(System.currentTimeMillis());

        for (Group mGroup : Groups.values()) {

            int max = mGroup.Students.size();
            Object[] map = mGroup.Students.keySet().toArray();

            Student mStudent = mGroup.Students.get(map[rnd.nextInt(max)]);

            mGroup.SelectHead(mStudent);
        }
    }

    void PrinStudents(){
        for (Student mStudent : Students) {
            System.out.println( mStudent.Fio + ", " + mStudent.Group.Title + ", " + mStudent.MarksAverage());
        }
    }

    void Unloading(){

        File f = new File("UnloadingDekanat.json");

        JSONObject JSONtitle = new JSONObject();

        JSONArray JSONstudents = new JSONArray();

        for (Student mStudent : Students) {

            JSONObject obj = new JSONObject();
            obj.put("id", mStudent.ID);
            obj.put("fio", mStudent.Fio);
            obj.put("groups", mStudent.Group.Title);

            JSONstudents.add(obj);
        }

        JSONtitle.put("students", JSONstudents);
        FileWriter file = null;

        try {
            file = new FileWriter(f);
            file.write(JSONtitle.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
