package hust.aco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Visualization extends JPanel {
    AntColonyOptimization aco;

    private int animationStepTime = 10;
    Timer timer;

    public Visualization(int width, int height, AntColonyOptimization aco) {
        this.setBackground(new Color(255, 255, 255));
        this.aco = aco;
        this.timer = new javax.swing.Timer(animationStepTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //draw particles
        g2d.setColor(new Color(0, 0, 0));
        ArrayList<City> cities = aco.getCities();
        for (City city : cities) {
            g.fillOval((int) (city.getX()), (int) (city.getY()), 10, 10);
        }
        for (int i = 0; i < aco.getNumCities() - 1; i++) {
            g.drawLine(cities.get(aco.getBestTour()[i]).getX(), cities.get(aco.getBestTour()[i]).getY(), cities.get(aco.getBestTour()[i + 1]).getX(), cities.get(aco.getBestTour()[i + 1]).getY());
        }
        g.drawLine(cities.get(aco.getBestTour()[0]).getX(), cities.get(aco.getBestTour()[0]).getY(), cities.get(aco.getBestTour()[aco.getNumCities() - 1]).getX(), cities.get(aco.getBestTour()[aco.getNumCities() - 1]).getY());
    }

}
