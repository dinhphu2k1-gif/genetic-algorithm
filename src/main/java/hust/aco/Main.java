package hust.aco;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        AntColonyOptimization antColonyOptimization = new AntColonyOptimization();

        long t1 = System.currentTimeMillis();
        antColonyOptimization.solve(3);
        long t2 = System.currentTimeMillis();
        System.out.println("time run: " + (t2 - t1) + " ms");

        int width = 1500;
        int height = 1000;
        Visualization visualization = new Visualization(width, height, antColonyOptimization);
        JFrame imgFrame = new JFrame();
        imgFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgFrame.setSize(width, height);
        imgFrame.add(visualization);
        visualization.setMinimumSize(new Dimension(width, height));
        visualization.setPreferredSize(new Dimension(width, height));
        imgFrame.setVisible(true);
    }
}
