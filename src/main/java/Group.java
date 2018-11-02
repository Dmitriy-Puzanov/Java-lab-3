import java.util.ArrayList;

class Group {
    private String title;
    private ArrayList<Student> students;
    private Student head;

    Group(String title){
        this.title = title;
        students = new ArrayList<Student>(30);
    }

    //Добавление в группу студента
    void addStudent(Student student){
        if (student != null) {
            students.add(student);
        }
    }

    //Удаление из группы студента
    void removeStudent(Student student){
        students.remove(student);
    }

    //Назначение старосты
    void setHead(Student head) {
        this.head = head;
    }

    //Поиск студента
    Student searchStudent(int id){
        if (students.size() < 1)
            return null;
        for (Student student:students
        ) {
            if (student.getId() == id)
                return student;
        }
        return null;
    }

    //Поиск студента
    Student searchStudent(String fio){
        if (students.size() < 1)
            return null;
        for (Student student:students
        ) {
            if (student.getFio().compareTo(fio) == 0)
                return student;
        }
        return null;
    }

    //Возвращает средний балл в группе
    double getAverageMark(){
        if (students.size() < 1)
            return 0;
        double sum = 0;
        for (Student student:students
        ) {
            sum += student.getAverageMark();
        }
        return sum / students.size();

    }

    //Возвращает название группы
    String getTitle() {
        return title;
    }

    //Возвращает список студентов
    ArrayList<Student> getStudents() {
        return students;
    }

    //Возвращает ссылку на старосту
    Student getHead() {
        return head;
    }

    //Возвращает количество студентов в группе
    int getAmountStudents(){
        return students.size();
    }
}
