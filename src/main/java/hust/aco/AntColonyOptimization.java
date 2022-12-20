package hust.aco;

import java.util.*;
import java.util.stream.IntStream;

public class AntColonyOptimization {
    // khởi tạo tham số
    private final static double c = 1.0;
    private final static double ALPHA = 1; // kiểm soát mức độ quan trọng của pheromone
    private final static double BETA = 5; // kiểm soát mức độ ưu tiên khoảng cách
    private final static double p = 0.5; // tốc độ bay hơi
    private final static double Q = 100.0; // hằng số kinh nghiệm, tổng lượng pheromone còn lại trên đường đi
    private final static int NUM_ANTS = 100; // số lượng con kiến trong tổ

    private double randomFactor = 0.01;

    private Random random = new Random();

    private ArrayList<City> cities;

    private int numCities;

    private double[][] matrix;

    private double[][] pheromones;

    private double[] probabilities;

    private int currentIndex;

    private List<Ant> ants = new ArrayList<>();

    private int[] bestTour;

    private double bestTourLength = Double.MAX_VALUE;

    public AntColonyOptimization() {
        cities = Route.initRoute;
        numCities = Route.numCities;
        matrix = Route.initMatrix();
        pheromones = new double[numCities][numCities];
        probabilities = new double[numCities];

        IntStream.range(0, NUM_ANTS)
                .forEach(i -> {
                    ants.add(new Ant(numCities));
                });
    }

    public void solve(int maxIterator) {
        setupAnts();
        initPheromones();

        for (int i = 0; i < maxIterator; i++) {
            moveAnts();
            updatePheromones();
            updateBestSolution();
        }

        System.out.println("best tour: " + showBestTour());
        System.out.println("best tour length: " + bestTourLength);
    }

    public String showBestTour() {
        String tour = "";
        for (int i = 0; i < numCities; i++) {
            tour += cities.get(bestTour[i]).toString() + ", ";
        }

        return tour;
    }

    /**
     * khởi tạo đàn kiến với điểm xuất phát ngẫu nhiên
     */
    public void setupAnts() {
        for (Ant ant : ants) {
            ant.clear();
            ant.visitCity(-1, random.nextInt(numCities));
        }

        currentIndex = 0;
    }

    /**
     * chọn cách di chuyển cho đàn kiến
     */
    public void moveAnts() {
        IntStream.range(currentIndex, numCities - 1)
                .forEach(i -> {
                    ants.forEach(ant -> ant.visitCity(currentIndex, selectNextCity(ant)));
                    currentIndex++;
                });
    }

    /**
     * chọn city tiếp cho kiến k
     *
     * @param ant
     * @return
     */
    public int selectNextCity(Ant ant) {
        int t = random.nextInt(numCities - currentIndex);
        if (random.nextDouble() < randomFactor) {
            OptionalInt cityIndex = IntStream.range(0, numCities)
                    .filter(i -> i == t && !ant.visited(i))
                    .findFirst();
            if (cityIndex.isPresent()) {
                return cityIndex.getAsInt();
            }
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }

        throw new RuntimeException("There are no other cities");
    }

    /**
     * tính xác suất chọn thành phố tiếp theo của kiến k
     */
    public void calculateProbabilities(Ant ant) {
        int from = ant.trail[currentIndex]; // thành phố hiện tại của kiến k đang đứng
        double denominator = 0.0;

        for (int to = 0; to < numCities; to++) {
            if (!ant.visited(to)) {
                denominator += Math.pow(pheromones[from][to], ALPHA) * Math.pow(1.0 / matrix[from][to], BETA);
            }
        }

        for (int to = 0; to < numCities; to++) {
            if (ant.visited(to)) {
                probabilities[to] = 0.0;
            } else {
                double numerator = Math.pow(pheromones[from][to], ALPHA) * Math.pow(1.0 / matrix[from][to], BETA);
                probabilities[to] = numerator / denominator;
            }
        }
    }

    /**
     * cập nhật pheromone
     */
    public void updatePheromones() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] *= 1 - p;
            }
        }

        for (Ant ant : ants) {
            double contribution = Q / ant.trailLength(matrix);
            for (int i = 0; i < numCities - 1; i++) {
                int from = ant.trail[i];
                int to = ant.trail[i + 1];
                pheromones[from][to] += contribution;
            }

            pheromones[ant.trail[numCities - 1]][ant.trail[0]] += contribution;
        }
    }

    /**
     * cập nhật solution
     */
    public void updateBestSolution() {
        if (bestTour == null) {
            bestTour = ants.get(0).trail;
            bestTourLength = ants.get(0).trailLength(matrix);
        }

        for (Ant ant : ants) {
            double length = ant.trailLength(matrix);
            if (length < bestTourLength) {
                bestTourLength = length;
                bestTour = ant.trail;
            }
        }
    }

    /**
     * khởi tạo pheromone
     */
    private void initPheromones() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] = c;
            }
        }
    }


    public static void main(String[] args) {
        AntColonyOptimization antColonyOptimization = new AntColonyOptimization();
        antColonyOptimization.solve(3);
    }
}
