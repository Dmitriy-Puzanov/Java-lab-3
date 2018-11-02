
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created with IntelliJ IDEA.
 * User: Nasibulin
 * Date: 15.10.18
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
public class Student {
    private int id;
    private String fio;
    private Group group;
    private List<Integer> marks = new ArrayList<Integer>();
    private int num;
    private final String NO_MARKS_YET = "У студента еще нет оценок.";

    public Student(int id, String fio) {
        this.id = id;
        this.fio = fio;
    }

    public void addMark(int mark) {
        marks.add(mark);
        num++;
    }

    public double avgMark() {
        double avgMark = 0;
        try {
            avgMark = marks.stream().mapToInt(e -> e).average().getAsDouble();
        } catch (NoSuchElementException ex) {
            //ex.printStackTrace();
            System.err.println(NO_MARKS_YET);
        }
        return avgMark;
    }
    public int minMark() {
        int avgMark = 0;
        try {
            avgMark = marks.stream().mapToInt(e -> e).min().getAsInt();
        } catch (NoSuchElementException ex) {
            //ex.printStackTrace();
            System.err.println(NO_MARKS_YET);
        }
        return avgMark;
    }
    public int maxMark() {
        int avgMark = 0;
        try {
            avgMark = marks.stream().mapToInt(e -> e).max().getAsInt();
        } catch (NoSuchElementException ex) {
            //ex.printStackTrace();
            System.err.println(NO_MARKS_YET);
        }
        return avgMark;
    }

    public void setGroup(Group group) {
        this.group=group;
    }

    public String getFio() {
        return fio;
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public Group getGroup(){
        return group;
    }

    public List<Integer> getMarks() {
        return marks;
    }

    public String toString() {
        return fio;
    }

}
