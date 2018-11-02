import java.util.ArrayList;

class Student {
    private long id;
    private String fio;
    private Group group;
    private ArrayList<Integer> marks;

    Student(long id, String fio){
        this.id = id;
        this.fio = fio;
        marks = new ArrayList<Integer>();
    }

    //Выдаёт количество оценок у студента
    int getNum(){
        return marks.size();
    }

    //Выдаёт средний балл
    double getAverageMark(){
        if (getNum() < 1)
            return 0;
        double sum = 0;
        for (int item:marks
             ) {
            sum+=item;
        }
        return sum / getNum();
    }

    //Добавляет новую оценку
    void addMark(int mark){
        if (mark > 1 && mark < 6)
            marks.add(mark);
    }

    //Установка ссылки на группу
    void setGroup(Group group) {
        this.group = group;
    }

    //Возвращает id
    long getId() {
        return id;
    }

    //Возвращает fio
    String getFio() {
        return fio;
    }

    //Возвращает ссылку на группу
    Group getGroup() {
        return group;
    }
}
