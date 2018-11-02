public class DekanatDemo {
    public static void main(String[] args) {
        Dekanat dekanat = new Dekanat();
        dekanat.setRandomMarks(10);
        dekanat.printInfo();
        dekanat.removeBadStudents();
        dekanat.balanceStudents();
        dekanat.printInfo();
        dekanat.printToFile();
    }
}
