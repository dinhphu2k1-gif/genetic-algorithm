package hust.USV.pso;

import hust.USV.Partition;
import hust.USV.Target;

import java.util.ArrayList;
import java.util.List;

public class PSO {
    public int NUM_TARGET;

    public List<Partition> partitions;

    public List<Particle> particles;

    public void PSOAlgorithm(Partition partition) {
        int psize = 1;
        for(Target target : partition.targetList) {
            psize += target.w;
        }

        for (int i = 0; i < NUM_TARGET; i++){
            for (int j = 0; j < psize; j++) {

            }
        }
    }

    public void initRoute() {
        partitions = new ArrayList<>();


    }




}
