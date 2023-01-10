package hust.USV;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class RandomTestCase {
    Random random = new Random();

    int numTarget;

    public RandomTestCase(int numTarget) {
        this.numTarget = numTarget;
    }

    public void generateTest() {
        String fileName = String.format("usv/test_case_%s.txt", numTarget);
        File file = new File(fileName);
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            int t = 0;
            while (t < numTarget) {
                int x = random.nextInt(1000);
                int y = random.nextInt(1000);
                int w = random.nextInt(3) + 1;

                String content = t + " " + x + " " + y + " " + w + "\n";
                System.out.print(content);
                outputStream.write(content.getBytes());

                t++;
            }

            System.out.println(String.format("finish generate %s target", numTarget));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RandomTestCase randomTestCase = new RandomTestCase(30);
        randomTestCase.generateTest();
    }
}
