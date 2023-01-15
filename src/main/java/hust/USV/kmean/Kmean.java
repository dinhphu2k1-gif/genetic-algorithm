package hust.USV.kmean;

import hust.USV.Partition;
import hust.USV.Target;
import hust.USV.Visualization;

import java.awt.print.Paper;
import java.io.File;
import java.util.*;

public class Kmean {
    public List<Target> targets; // danh sách các mục tiêu
    public int NUM_PARTITION;
    public int NUM_TARGET;
    public List<Partition> partitions;
    public Random random = new Random();
    public final static int MAX_ITERATION = 100;
    public final static double T_h = 600; // ngưỡng chênh lệnh tải
    public Visualization visualization;
    public double Jmin = Double.MAX_VALUE;
    public List<Partition> partitionFinal = new ArrayList<>();

    public Kmean(List<Target> targetList, int numPartition) {
        this.targets = targetList;
        this.NUM_TARGET = targets.size();
        this.NUM_PARTITION = numPartition;
        this.partitions = new ArrayList<>();

        visualization = new Visualization(1100, 1100, this);
    }

    public void clear() {
        partitions.clear();
    }

    public void initCentroid() {
        for (int i = 0; i < NUM_PARTITION; i++) {
            Partition partition = new Partition();
            partitions.add(partition);
        }

        boolean done = true;
        while (done) {
            done = false;
            for (Target target : targets) {
                // chọn ngẫu nhiên 1 partition để ném nó vào
                Partition partition = partitions.get(random.nextInt(NUM_PARTITION));
                partition.addTarget(target);
            }

            for (Partition partition : partitions) {
                if (partition.targetList.size() == 0) done = true;
            }
        }

        List<Centroid> centroids = getNewCentroid();
        for (int i = 0; i < NUM_PARTITION; i++) {
            Partition partition = partitions.get(i);
            Centroid centroid = centroids.get(i);
//            System.out.println("init centroid -> x: " + centroid.x + "\ty: " + centroid.y);

            partition.setCentroid(centroid);
            partition.reset();
        }
    }

    public List<Partition> KmeanAlgorithm() throws InterruptedException, CloneNotSupportedException {
        int t = 0;
        while (t < MAX_ITERATION) {
            System.out.println("t : " + t);

            clear();
            initCentroid();
            boolean done = false;
            while (true) {

                try {
                    partitionTarget();

                    List<Centroid> newCentroids = getNewCentroid();
                    // nếu không thay đổi tâm thì dừng lại
                    if (hasConverge(newCentroids)) {
                        System.out.println("da hoi tu");
                        done = true;
                        break;
                    }
//                    Thread.sleep(1000);

                    for (int i = 0; i < NUM_PARTITION; i++) {
                        Partition partition = partitions.get(i);
                        // nếu tiếp tục thay đổi tâm thì phải reset lại danh sách target
                        partition.reset();
                        partition.setCentroid(newCentroids.get(i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (done) {
                calculateTaskLoad();
                differeceTaskLoad();
            }

            System.out.println("Jmin : " + Jmin);
            t++;

            visualization.repaint();
            Thread.sleep(50);
        }

        return partitionFinal;
    }


    public double costFunction() {
        double J = 0;
        for (Partition partition : partitions) {
            for (Target target : partition.targetList) {
                J += target.w * getDistance(partition.centroid, target);
            }
        }

        return J;
    }

    /**
     * Tính độ chênh lệch tải giữa các USV
     */
    public void differeceTaskLoad() throws CloneNotSupportedException {
        int K = NUM_PARTITION;
        double T_d = 1 * 1.0 / (K * (K - 1));

        double t = 0;
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < K; j++) {
                if (i == j) continue;
                double T_i = partitions.get(i).taskLoad;
                double T_j = partitions.get(j).taskLoad;
                t += Math.abs(T_i - T_j);
            }
        }

        T_d *= t;
        System.out.println("T_d: " + T_d);

        if (T_d <= T_h) {
            double J = costFunction();
            if (J == 0) return;
            if (J < Jmin) {
                Jmin = J;
                partitionFinal = new ArrayList<>(partitions);
                for (int i = 0; i < NUM_PARTITION; i++) {
                    List<Target> srcList = partitions.get(i).targetList;
                    partitionFinal.get(i).targetList = new ArrayList<>(srcList);
                }
            }
        }
    }

    /**
     * Tính khối lượng công việc trong phân vùng đó
     */
    public void calculateTaskLoad() {
        for (Partition partition : partitions) {
            double taskLoad = 0;
            List<Target> targetPartition = partition.targetList;

            int W = 0; // tổng trọng số của các mục tiêu
            for (Target target : targetPartition) {
                W += target.w;
            }

            int numTarget = targetPartition.size();
            for (int i = 0; i < numTarget; i++) {
                Target v_m = targetPartition.get(i);
                int w_m = v_m.w;
                for (int j = i + 1; j < numTarget; j++) {
                    Target v_j = targetPartition.get(j);
                    taskLoad += w_m * v_m.distance(v_j) * 1.0 / (W - w_m);
                }
            }

            partition.setTaskLoad(taskLoad);
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

            if (denominator == 0) continue;
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

            File file = new File("usv/test_case_50.txt");
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


            Kmean kmean = new Kmean(targets, 5);
            kmean.KmeanAlgorithm();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
