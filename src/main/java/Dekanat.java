import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.net.*;
import java.util.*;

class Dekanat {
    private ArrayList<Student> students;
    private ArrayList<Group> groups;

    Dekanat(){
        createStudentsFromFile();
        createGroupsFromFile();
        distributeStudents();

        for (Group group:groups
        ) {
            choiceHead(group);
        }
        System.out.println("Создан новый деканат.\n");
    }
    //Создание списка студентов из файла
    private void createStudentsFromFile(){
        students = new ArrayList<Student>(30);
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("students.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            JSONParser parser=new JSONParser();
            JSONObject js=(JSONObject)parser.parse(reader);
            JSONArray items=(JSONArray)js.get("students");

            for(Object i : items) {
                long id = (Long) ((JSONObject)i).get("id");
                String fio = (String) ((JSONObject)i).get("fio");
                students.add(new Student(id, fio));
            }

        }catch (Exception e){
            System.out.println("Error create students from file");
            System.out.println(e);
        }

    }

    //Создание списка групп из файла
    private void createGroupsFromFile(){
        groups = new ArrayList<Group>(3);
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("groups.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            JSONParser parser=new JSONParser();
            JSONObject js=(JSONObject)parser.parse(reader);
            JSONArray items=(JSONArray)js.get("groups");

            for(Object i : items) {
                String title = (String) ((JSONObject)i).get("title");
                groups.add(new Group(title));
            }

        }catch (Exception e){
            System.out.println("Error create groups from file");
            System.out.println(e);
        }

    }

    //Распределение студентов по группам
    private void distributeStudents(){
        int pointer = 0;
        for (Student student:students
             ) {
            if (pointer == groups.size())
                pointer = 0;

            studentRegistration(student, groups.get(pointer));

            pointer++;
        }
    }

    //Зачисление студента
    private void studentRegistration(Student student, Group group) {
        student.setGroup(group);
        group.addStudent(student);
    }

    //Выбор старосты
    private void choiceHead(Group group){
        ArrayList<Student> groupStudents = group.getStudents();
        Random r = new Random();
        int pointer = r.nextInt(groupStudents.size());
        group.setHead(groupStudents.get(pointer));
    }

    //Выставление оценок
    void setRandomMarks(int iterations){
        Random r = new Random();
        for (int i = 0; i < iterations; i++) {
            for (Student student:students
            ) {
                int mark = r.nextInt(4) + 2;
                student.addMark(mark);
            }
        }
    }

    //Отчисление отстающих студентов
    void removeBadStudents(){
        for (Student student: students
             ) {
            if(student.getAverageMark() < 3.0){
                Group group = student.getGroup();
                group.removeStudent(student);
                student.setGroup(null);
                System.out.println("\nСтудент: " + student.getFio() + " отчислен из группы: " + group.getTitle());
                System.out.printf("со средним баллом: %.2f", student.getAverageMark());
                System.out.println();
                if (group.getHead() == student){
                    choiceHead(group);
                    System.out.println("\nВ группе: " + group.getTitle() + " проведено переизбрание старосты.");
                }
            }
        }
    }

    //Балансировка студентов в группах
    void balanceStudents(){
        int minStudentsGroup = groups.get(0).getAmountStudents();
        int numberMinGroup = 0;
        int maxStudentsGroup = groups.get(0).getAmountStudents();
        int numberMaxGroup = 0;
        Random r = new Random();
        
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getAmountStudents() < minStudentsGroup){
                minStudentsGroup = groups.get(i).getAmountStudents();
                numberMinGroup = i;
            }
            if (groups.get(i).getAmountStudents() > maxStudentsGroup){
                maxStudentsGroup = groups.get(i).getAmountStudents();
                numberMaxGroup = i;
            }
        }
        
        if(minStudentsGroup == maxStudentsGroup){
            int lastGroup = groups.size() - 1;
            Group groupFrom = groups.get(lastGroup);
            ArrayList<Student> studentsGroupFrom = groupFrom.getStudents();
            int index = r.nextInt(studentsGroupFrom.size());
            Student relocating = studentsGroupFrom.get(index);
            transferStudent(relocating, groups.get(0));
            System.out.println("\nСтудент: " + relocating.getFio() + " переведён из группы:");
            System.out.println(groupFrom.getTitle() + " в группу: " + groups.get(0).getTitle() + " для профилактики.\n");
        }else {
            Group groupFrom = groups.get(numberMaxGroup);
            ArrayList<Student> studentsGroupFrom = groupFrom.getStudents();
            int index = r.nextInt(studentsGroupFrom.size());
            Student relocating = studentsGroupFrom.get(index);
            transferStudent(relocating, groups.get(numberMinGroup));
            System.out.println("\nСтудент: " + relocating.getFio() + " переведён из группы:");
            System.out.println(groupFrom.getTitle() + " в группу: " + groups.get(numberMinGroup).getTitle() + " при балансировке групп.\n");
        }
    }

    //Перевод студента в другую группу
    private void transferStudent(Student student, Group newGroup){
        Group oldGroup = student.getGroup();
        oldGroup.removeStudent(student);
        student.setGroup(newGroup);
        newGroup.addStudent(student);
    }

    //Вывод статистики на экран
    void printInfo(){
        System.out.println("Информация:");
        for (Group group:groups
             ) {
            System.out.println("\nГруппа: " + group.getTitle());
            System.out.println("Количество студентов: " + group.getAmountStudents());
            System.out.printf("Средний балл: %.2f\n", group.getAverageMark());
            System.out.println("Староста: " + group.getHead().getFio());
        }
    }

    //Запись результатов в файл
    void printToFile(){
        JSONObject result = new JSONObject();
        JSONArray resultArray = new JSONArray();
        for (Group group:groups
             ) {
            JSONArray jsonStudents = new JSONArray();
            ArrayList<Student> studentsGr = group.getStudents();
            for (Student student:studentsGr
                 ) {
                JSONObject jsonStudent = new JSONObject();
                jsonStudent.put("ID", student.getId());
                jsonStudent.put("FIO", student.getFio());
                String averageMark = String.format("%.2f", student.getAverageMark());
                jsonStudent.put("Average Mark", averageMark);
                jsonStudents.add(jsonStudent);
            }
            JSONObject jsonGroup = new JSONObject();
            jsonGroup.put("Title", group.getTitle());
            jsonGroup.put("Head", group.getHead().getFio());
            jsonGroup.put("Amount Students", group.getAmountStudents());
            String averageMark = String.format("%.2f", group.getAverageMark());
            jsonGroup.put("Average Mark", averageMark);
            jsonGroup.put("Students", jsonStudents);
            resultArray.add(jsonGroup);
        }
        result.put("Groups", resultArray);

        try {
            FileWriter file = new FileWriter("result.json");
            file.write(result.toJSONString());
            file.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
