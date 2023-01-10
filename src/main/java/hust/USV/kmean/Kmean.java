package hust.USV.kmean;

import hust.USV.Partition;
import hust.USV.Target;
import hust.USV.Visualization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Kmean {
    public List<Target> targets; // danh sách các mục tiêu
    public int NUM_PARTITION;
    public int NUM_TARGET;
    public List<Partition> partitions;
    public Random random = new Random();
    public final static int MAX_ITERATION = 100;
    public Visualization visualization;

    public Kmean(List<Target> targetList, int numPartition) {
        this.targets = targetList;
        this.NUM_TARGET = targets.size();
        this.NUM_PARTITION = numPartition;
        this.partitions = new ArrayList<>();

        visualization = new Visualization(1100, 1100, this);
    }

    public void initCentroid() {
        for (int i = 0; i < NUM_PARTITION; i++){
            Partition partition = new Partition();
            partitions.add(partition);
        }

        for (Target target : targets) {
            // chọn ngẫu nhiên 1 partition để ném nó vào
            Partition partition = partitions.get(random.nextInt(NUM_PARTITION));
            partition.addTarget(target);
        }

        List<Centroid> centroids = getNewCentroid();
        for (int i = 0; i < NUM_PARTITION; i++) {
            Partition partition = partitions.get(i);
            Centroid centroid = centroids.get(i);
            System.out.println("init centroid -> x: " + centroid.x + "\ty: " + centroid.y);

            partition.setCentroid(centroid);
            partition.reset();
        }
    }

    public void KmeanAlgorithm() throws InterruptedException {
        int t = 0;
//        while (t < MAX_ITERATION) {
//        }
        initCentroid();

        while (true) {
            partitionTarget();

            List<Centroid> newCentroids = getNewCentroid();
            // nếu không thay đổi tâm thì dừng lại
            if (hasConverge(newCentroids)){
                System.out.println("da hoi tu");
                break;
            }
            visualization.repaint();
            System.out.println("repaint");
            Thread.sleep(3000);

            for (int i = 0; i < NUM_PARTITION; i++) {
                Partition partition = partitions.get(i);
                // nếu tiếp tục thay đổi tâm thì phải reset lại danh sách target
                partition.reset();
                partition.setCentroid(newCentroids.get(i));
            }
        }
    }

    /**
     * Tính khối lượng công việc trong phân vùng đó
     */
    public void calculateTaskLoad() {
        for (Partition partition : partitions) {
            List<Target> targetPartition = partition.targetList;

            int W = 0; // tổng trọng số của các mục tiêu
            for (Target target : targetPartition) {
                W += target.w;
            }

            int numTarget = targetPartition.size();

        }
    }

    /**
     * Kiểm tra xem thuật toán Kmean đã hội tụ hay chưa
     */
    public boolean hasConverge(List<Centroid> newCentroids) {
        for (int i = 0; i < NUM_PARTITION; i++) {
            Centroid oldCentroid = partitions.get(i).centroid;
            Centroid newCentroid = newCentroids.get(i);

            if (!oldCentroid.equals(newCentroid)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Lấy tâm mới cho các partition
     */
    public List<Centroid> getNewCentroid() {
        List<Centroid> newCentroids = new ArrayList<>();
        for (Partition partition : partitions) {
            int numeratorX = 0;
            int numeratorY = 0;
            int denominator = 0;
            for (Target target : partition.targetList) {
                numeratorX += target.w * target.x;
                numeratorY += target.w * target.y;
                denominator += target.w;
            }

            int newX = numeratorX / denominator;
            int newY = numeratorY / denominator;

            Centroid centroid = new Centroid(newX, newY);
            newCentroids.add(centroid);
        }

        return newCentroids;
    }

    /**
     * Đẩy các mục tiêu về các phân vùng
     */
    public void partitionTarget() {
        for (Target target : targets) {
            Partition partitionNearest = getPartitionNearst(target);
            partitionNearest.addTarget(target);


        }
    }


    /**
     * Lấy phân vùng gần với mục tiêu nhất
     */
    public Partition getPartitionNearst(Target target) {
        Partition partitionNearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Partition partition : partitions) {
            double distance = getDistance(partition.centroid, target);

            if (distance < minDistance) {
                partitionNearest = partition;
                minDistance = distance;
            }
        }

        return partitionNearest;
    }

    /**
     * Đo khoảng cách giữa tâm và các mục tiêu
     */
    public double getDistance(Centroid centroid, Target target) {
        return Math.sqrt(Math.pow(centroid.x - target.x, 2) + Math.pow(centroid.y - target.y, 2));
    }


    public static void main(String[] args) {
        try {
            List<Target> targets = new ArrayList<>();

            File file = new File("usv/test_case_30.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                if (!scanner.hasNext()) break;

                String name = scanner.next();
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int w = scanner.nextInt();

//                System.out.println(name + "\t" + x + "\t" + y + "\t" + w);
                Target target = new Target(name, x, y, w);
                targets.add(target);
            }


            Kmean kmean = new Kmean(targets, 3);
            kmean.KmeanAlgorithm();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
