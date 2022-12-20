package hust.pso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Visualization extends JPanel {
    PSO pso;

    private int animationStepTime = 10;
    Timer timer;

    public Visualization(int width, int height, PSO pso) {
        this.setBackground(new Color(255, 255, 255));
        this.pso = pso;
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
        for (City city : pso.map) {
            g.fillOval((int) (city.getX()), (int) (city.getY()), 10, 10);
        }
        for (int i = 0; i < pso.CITY_COUNT - 1; i++) {
            g.drawLine(pso.map.get(pso.particles.get(0).getData()[i]).getX(), pso.map.get(pso.particles.get(0).getData()[i]).getY(), pso.map.get(pso.particles.get(0).getData()[i + 1]).getX(), pso.map.get(pso.particles.get(0).getData()[i + 1]).getY());
        }
        g.drawLine(pso.map.get(pso.particles.get(0).getData()[0]).getX(), pso.map.get(pso.particles.get(0).getData()[0]).getY(), pso.map.get(pso.particles.get(0).getData()[pso.CITY_COUNT - 1]).getX(), pso.map.get(pso.particles.get(0).getData()[pso.CITY_COUNT - 1]).getY());
    }
}
