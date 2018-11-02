import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;

class Student {

    public String FIO;
    public int IDStudent;
    public int GroupID;
    public String TitleGroup;
    public double MediumMark;
    public ArrayList<Integer> Marks;

    Student (String FIO, int IDStudent, int GroupID, String TitleGroup) {
        this.FIO = FIO;
        this.IDStudent = IDStudent;
        this.GroupID = GroupID;
        this.TitleGroup = TitleGroup;
        Marks = new ArrayList<Integer>();
        this.MediumMark=0;
    }

    public static void addMarks() {

        for (int i = 0; i < Dekanat.studs.size(); i++) {
            Student student = Dekanat.studs.get(i);
            for (int k=0;k<5;k++){
                student.Marks.add(2 + (int) (Math.random() * 4));

            }

        }
    }

    public static void calcStudMediumMark(){

        double sum = 0;

        for (int i = 0; i < Dekanat.studs.size(); i++) {
            Student student = Dekanat.studs.get(i);
            for (int k=0;k<student.Marks.size();k++)
                sum += student.Marks.get(k);
            student.MediumMark = sum/student.Marks.size();
            sum=0;
        }

    }

}

class Group{
    public String TitleGroup;
    public static Student stud;
    public ArrayList<Student> student;
    public double GroupMediummark;
    public int Hide;

    Group(String TitleGroup, int Hide) {
        this.TitleGroup = TitleGroup;
        student = new ArrayList<Student>();
        this.Hide=Hide;
        this.GroupMediummark=0;
    }

    public static void addStudent(Student s) {
        stud = s;
        Group group = Dekanat.groups.stream().filter(groups -> groups.TitleGroup.equals(stud.TitleGroup)).findFirst().orElse(null);
        group.student.add(stud);
    }

    public static void initHide(){
        Random r = new Random();
        for (int i = 0; i<Dekanat.groups.size();i++){
            Group group = Dekanat.groups.get(i);
            stud =   group.student.get(r.nextInt(group.student.size()));
            group.Hide = stud.IDStudent;
            Dekanat.infoDekanat.offer("Староста в группе " + group.TitleGroup + " "  + stud.FIO + " " + "IDStudent " + group.Hide);
        }

    }

    public static void calcGroupMediumMark(){

        double sum = 0;

        for (int i = 0; i < Dekanat.groups.size(); i++) {
            Group group = Dekanat.groups.get(i);
            for (int k=0;k<group.student.size();k++) {
                stud = group.student.get(k);
                sum += stud.MediumMark;
            }
            group.GroupMediummark = sum/group.student.size();
            Dekanat.infoDekanat.offer("Средний балл успеваемости в группе " + group.TitleGroup + " "  + String.format("%.2f", group.GroupMediummark));
            sum=0;
        }

    }
    public static void removeStudent(){
        int count = 0;
        Dekanat.infoDekanat.offer("За плохую успеваемость были отчислены: ");
        for (int i = 0; i < Dekanat.groups.size(); i++) {
            Group group = Dekanat.groups.get(i);
            for (int k=0;k<group.student.size();k++) {
                stud = group.student.get(k);
                if (stud.MediumMark < 2.9){
                    count++;
                    group.student.remove(k);
                Dekanat.infoDekanat.offer("Студент " + stud.FIO + " из группы "  + group.TitleGroup + " Средний балл успеваемости " + stud.MediumMark);
                }

            }
        }
        if (count ==0)
            Dekanat.infoDekanat.offer("Все студенты успевают хорошо, отчисленных нет");
    }

    public static Student searchStudent(String fio) {

        for (int i = 0; i < Dekanat.groups.size(); i++) {
            Group group = Dekanat.groups.get(i);
            for (int k=0;k<group.student.size();k++) {
                stud = group.student.get(k);
                if (fio.replace("\"","").equals((stud.FIO.replace("\"","")).toString()))
                    return stud;
            }
        }
        return null;
    }

    public static void moveStudent(){

int count=0;

       for (Group i: Dekanat.groups) {
           Group group = i;

               for (Iterator<Student> iter = group.student.listIterator(); iter.hasNext(); ) {
                   stud = iter.next();
                   count++;
                   Dekanat.infoDekanat.offer("Студент " + stud.FIO + " из группы " + group.TitleGroup);
                   if (count < 2) {
                       iter.remove();
                       group = Dekanat.groups.get(2);
                       group.student.add(stud);
                       stud.TitleGroup = group.TitleGroup;
                       stud.GroupID = 3;
                       Dekanat.infoDekanat.offer("был переведен в группу " + group.TitleGroup);
                       break;
                   }
                    else if (count <4) {
                       iter.remove();
                       group = Dekanat.groups.get(0);
                       group.student.add(stud);
                       stud.TitleGroup = group.TitleGroup;
                       stud.GroupID = 1;
                       Dekanat.infoDekanat.offer("был переведен в группу " + group.TitleGroup);
                       break;
                   }
                   else
                       break;
               }
       }
     }

}

public class Dekanat {
    static ArrayList<Student> studs = new ArrayList<>();
    static ArrayList<Group> groups = new ArrayList<>();
    static Deque<String> infoDekanat = new ArrayDeque<String>();

    public static void main(String[] args) {

        String File = "student.json";
        String name = "Королева Д.У.";
        createStudents(File);
        dekanatinitHide();
        setaddMarks();
        setcalcStudMediumMark();
        setcalcGroupMediumMark();
        setremoveStudent();
        dekanatSearchStudent(name);
        setmoveStudent();
        dekanatinitHide();
        saveToFile();
        outInfo();

    }

    public static void createStudents(String File)
    {
        String TitleGroup_prev = "";

        try {

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream is = cl.getResourceAsStream(File);
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            JsonElement jsonElement = new JsonParser().parse(bf);
            JsonArray jsonArray= jsonElement.getAsJsonArray();
            if (jsonArray != null) {
                for (int i = 0; i< jsonArray.size(); i++){
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String FIO = String.valueOf((jsonObject.get("FIO")));
                    int IDStudent = Integer.parseInt(String.valueOf((jsonObject.get("IDStudent"))));
                    String TitleGroup = String.valueOf((jsonObject.get("TitleGroup")));

                    int GroupID = Integer.parseInt(String.valueOf((jsonObject.get("GroupID"))));
                    Student s = new Student(FIO, IDStudent, GroupID, TitleGroup);
                    studs.add(s);
                    if (!(TitleGroup.equals(TitleGroup_prev))){
                        TitleGroup_prev = TitleGroup;
                        Group g = new Group(TitleGroup,0);
                        groups.add(g);
                    }
                      Group.addStudent(s);


                }
            }

        }
        catch (Exception  e) {
            e.printStackTrace();
        }

    }

public static void dekanatSearchStudent(String fio){

    Student s = Group.searchStudent(fio);
    if (s!=null)
    Dekanat.infoDekanat.offer("Был найден студент " + s.FIO + " из группы " + s.TitleGroup + ", IDStudent: " + s.IDStudent + ", средний балл успеваемости "+s.MediumMark);
    else
        Dekanat.infoDekanat.offer("Искомый студент не найден");
}


public static void saveToFile(){

    JSONArray listMain = new JSONArray();
    JSONArray list = new JSONArray();
    for (Group i: Dekanat.groups) {
        Group group = i;
        JSONObject g = new JSONObject();
        g.put("Group:",group.TitleGroup);
            for (Student k : group.student) {
            Student s = k;
            JSONObject Student = new JSONObject();
            Student.put("FIO", s.FIO);
            Student.put("IDStudent", s.IDStudent);
            Student.put("GroupID", s.GroupID);
            Student.put("TitleGroup", s.TitleGroup);
            Student.put("MediumMark", s.MediumMark);
            Student.put("Marks", s.Marks);
            list.put(Student);
        }
        g.put("Student:",list);
        listMain.put(g);
    }
    try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(new File("src/main/resources/student_out.json"), listMain.toString());

        } catch (
            IOException e) {
        e.printStackTrace();
    }
}

public static void outInfo(){

    while(infoDekanat.size() != 0)   {
        System.out.println(infoDekanat.pop());
    }
}
    public static void dekanatinitHide() { Group.initHide(); }

    public static void setaddMarks() {Student.addMarks();}

    public static void setcalcStudMediumMark() {Student.calcStudMediumMark();}

    public static void setcalcGroupMediumMark() {Group.calcGroupMediumMark();}

    public static void setremoveStudent() {Group.removeStudent();}

    public static void setmoveStudent() {Group.moveStudent();}
}


