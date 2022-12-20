package hust.pso;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        PSO pso = new PSO();
        pso.getCities("tsp/test_case/test_case10.txt");
        pso.intializeMap();
        pso.PSOAlgorithm();
        pso.printBestSolution();

        int width = 1500;
        int height = 1000;
        Visualization visualization = new Visualization(width, height, pso);
        JFrame imgFrame = new JFrame();
        imgFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgFrame.setSize(width, height);
        imgFrame.add(visualization);
        visualization.setMinimumSize(new Dimension(width, height));
        visualization.setPreferredSize(new Dimension(width, height));
        imgFrame.setVisible(true);
    }
}
