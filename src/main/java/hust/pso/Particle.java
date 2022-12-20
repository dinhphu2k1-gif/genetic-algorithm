package hust.pso;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
@Setter
public class Particle implements Comparable<Particle> {
    private int name; // tên ở đây là vị trí thành phố trong test case
    private int data[];
    private double localBest = 0;
    private double velocity = 0.0;
    private ArrayList<City> tour = new ArrayList<>();

    public Particle(int CITY_COUNT) {
        data = new int[CITY_COUNT];
        this.localBest = 0;
        this.velocity = 0.0;
    }

    public int compareTo(@NotNull Particle that) {
        if (this.localBest < that.getLocalBest()) {
            return -1;
        } else if (this.localBest > that.getLocalBest()) {
            return 1;
        } else {
            return 0;
        }
    }

    public int data(int index) {
        return this.data[index];
    }

    public void data(int index, int value) {
        this.data[index] = value;
    }

    @Override
    public String toString() {
        return "Particle{ " +  name +  " }";
    }


}
