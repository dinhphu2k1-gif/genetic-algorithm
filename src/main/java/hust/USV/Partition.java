package hust.USV;

import hust.USV.kmean.Centroid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partition {
    public List<Target> targetList;

    public Centroid centroid;

    public double taskLoad;

    public Color color;

    public Random rand = new Random();

    public Partition() {
        this.targetList = new ArrayList<>();
        randomColor();
    }

    public Partition(Centroid centroid) {
        this.targetList = new ArrayList<>();
        this.centroid = centroid;

        randomColor();
    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }

    public void setTaskLoad(double taskLoad) {
        this.taskLoad = taskLoad;
    }

    public void addTarget(Target target) {
        targetList.add(target);
    }

    public void reset(){
        targetList = new ArrayList<>();
    }

    public void randomColor() {
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        color = new Color(r, g, b);
    }
}
