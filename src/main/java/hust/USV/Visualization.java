package hust.USV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hust.USV.Partition;
import hust.USV.kmean.Kmean;

public class Visualization extends JPanel{
    private int animationStepTime = 1;
    Timer timer;
    int width, height;
    Kmean kmean;

    public Visualization(int width, int height, Kmean kmean) {
        this.width = width;
        this.height = height;
        this.kmean = kmean;

        this.setBackground(new Color(255, 255, 255));
//        this.timer = new javax.swing.Timer(0, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
////                updatePartitions();
//                repaint();
//            }
//        });
//        timer.start();

        JFrame imgFrame = new JFrame();
        imgFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgFrame.setSize(width, height);
        imgFrame.add(this);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        imgFrame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Partition partition : kmean.partitions) {
            for (Target target : partition.targetList) {
                g.setColor(partition.color);
                g.fillOval(target.x, target.y, target.w * 10, target.w * 10);
            }
        }
    }

    public void updatePartitions() {

    }


}
