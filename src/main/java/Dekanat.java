
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nasibulin
 * Date: 15.10.18
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */
public class Dekanat {
    private static final String STUDENTS_FILE = "Students.json";
    private static final String GROUPS_FILE = "Groups.json";
    private static final String GROUPS_PATH = System.getProperty("user.dir") + "/db/" + GROUPS_FILE;
    private static final String STUDENTS_PATH = System.getProperty("user.dir") + "/db/" + STUDENTS_FILE;
    private static final String GROUPS_EXP = System.getProperty("user.dir") + "/db/new_" + GROUPS_FILE;
    private static final String STUDENTS_EXP = System.getProperty("user.dir") + "/db/new_" + STUDENTS_FILE;

    private List<Student> students = new ArrayList<Student>();
    private List<Group> groups = new ArrayList<Group>();


    void importGroups() {

        groups.clear();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(GROUPS_PATH));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray groupArray = (JSONArray) jsonObject.get("Groups");

            Iterator<JSONArray> iterator = groupArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                iterator.next();
                JSONObject groupData = (JSONObject) groupArray.get(i);
                int id = ((Long) groupData.get("GROUP_ID")).intValue();
                String title = (String) groupData.get("GROUP");
                groups.add(new Group(id, title));
                i++;
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    void importStudents() {

        students.clear();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(STUDENTS_PATH));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray studentArray = (JSONArray) jsonObject.get("Students");

            Iterator<JSONArray> iterator = studentArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                iterator.next();
                JSONObject studentData = (JSONObject) studentArray.get(i);
                int id = ((Long) studentData.get("ID")).intValue();
                String fio = (String) studentData.get("STUDENT");
                students.add(new Student(id, fio));
                i++;
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    void arrangeGroups() {

        Iterator<Student> iterator = students.iterator();
        for (Group group : groups) {
            group.clear();
            for (int i = 0; i < students.size() / groups.size(); i++) {
                if (!iterator.hasNext())
                    return;
                Student s = iterator.next();
                group.addStudent(s);
                s.setGroup(group);
                //System.out.println(group);
            }
        }
    }

    void addRandomMarks(int count) {
        Random rand = new Random();
        for (Student student : students) {
            for (int i = 0; i < count; i++)
                student.addMark(rand.nextInt(4) + 2);
        }
    }

    List<Group> getGroups() {
        return groups;
    }

    void allGroupElection() {
        for (Group group : groups) {
            group.headElection();
        }
    }

    Group searchGroup(String title) {
        for (Group group : groups) {
            if (group.getTitle().equals(title))
                return group;
        }
        return null;
    }

    Group searchGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id)
                return group;
        }
        return null;
    }

    Student searchStudent(int id) {

        for (Student student : students) {
            if (student.getId() == id)
                return student;
        }
        return null;
    }

    void transferStudent(Student student, Group toGroup) {
        Group fromGroup = student.getGroup();
        fromGroup.removeStudent(student);
        student.setGroup(toGroup);
        toGroup.addStudent(student);
    }

    void dismissStudents(double level) {

        Iterator<Student> iterator = students.iterator();

        while (iterator.hasNext()) {
            Student s = iterator.next();
            Group g = s.getGroup();
            if (s.avgMark() < level) {
                g.removeStudent(s);
                iterator.remove();
                if (s == g.getHead()) g.headElection();
            }
        }

    }

    int num() {
        return students.size();
    }

    void printData() {
        class Format {
            private Formatter f = new Formatter(System.out);

            private void printTitle(String title) {
                f.format("\n%-22s\n", title);
                f.format("-----------------------------------------------------------\n");
            }

            private void printHeader() {
                f.format("%-5s %-37s %-10s\n", "ID", "ФИО", "Средняя оценка");
                f.format("-----------------------------------------------------------\n");
            }

            private void printBody(int id, String fio, double avgMark) {
                f.format("%-5d %-36s %5.2f\n", id, fio, avgMark);
            }

            private void printFooter(double avgScore, String fio) {
                f.format("-----------------------------------------------------------\n");
                f.format("%-42s %5.2f\n", "Средняя оценка в группе:", avgScore);
                f.format("%-10s %-36s\n", "Староста группы:", fio);
            }
        }
        Format format = new Format();
        for (Group group : groups) {
            format.printTitle(group.getTitle());
            format.printHeader();
            for (Student student : group.getStudents())
                format.printBody(student.getId(), student.getFio(), student.avgMark());
            format.printFooter(group.avgScore(), group.getHead().getFio());
        }
    }
    void exportStudents() {

        JSONObject jsonObject = new JSONObject();
        JSONArray studentArray = new JSONArray();
        for (Student student:students){
            JSONObject studentData = new JSONObject();
            studentData.put("STUDENT", student.getFio());
            studentData.put("ID", student.getId());
            studentData.put("GROUP_ID",student.getGroup().getId());
            studentArray.add(studentData);
        }
        jsonObject.put("Students", studentArray);
        try {
            FileWriter file = new FileWriter(STUDENTS_EXP);
            file.write(jsonObject.toJSONString());
            file.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void exportGroups() {

        JSONObject jsonObject = new JSONObject();
        JSONArray groupsArray = new JSONArray();
        for (Group group:groups){
            JSONObject groupsData = new JSONObject();
            groupsData.put("GROUP", group.getTitle());
            groupsData.put("GROUP_ID", group.getId());
            groupsArray.add(groupsData);
        }
        jsonObject.put("Groups", groupsArray);
        try {
            FileWriter file = new FileWriter(GROUPS_EXP);
            file.write(jsonObject.toJSONString());
            file.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }


}



