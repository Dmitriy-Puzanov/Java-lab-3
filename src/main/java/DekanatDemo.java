
/**
 * Created with IntelliJ IDEA.
 * User: Nasibulin
 * Date: 16.10.18
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class DekanatDemo {
    public static void main(String[] args) {
        Dekanat dek = new Dekanat();
        dek.importGroups();
        dek.importStudents();
        dek.arrangeGroups();
        dek.addRandomMarks(25);
        dek.allGroupElection();
        dek.printData();
        //dek.dismissStudents(3.48);
        dek.transferStudent(dek.searchStudent(10),dek.searchGroup(2));
        Student h = dek.searchGroup(1).getHead();
        dek.searchGroup(1).removeStudent(h);
        dek.dismissStudents(3.55);
        dek.printData();
        dek.exportStudents();
        dek.exportGroups();
    }
}
