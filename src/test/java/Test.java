import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        List<String> l1 = new ArrayList<>();
        l1.add(new String("a"));
        l1.add(new String("b"));

        List<String> l2 = new ArrayList<>(l1);
        l2.add(new String("c"));

        l1.clear();
        System.out.println(l2.size());
     }
}
