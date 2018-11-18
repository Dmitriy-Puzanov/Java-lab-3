import org.junit.Test;

public class TestDekanat {

    @Test
    public void main() {
        TestDekanat();
    }

    void TestDekanat() {
        Dekanat D1 = new Dekanat();
        D1.Download();
        D1.SelectHead();
        D1.Grading(D1.Students);
        D1.PrinStudents();
    }
}

