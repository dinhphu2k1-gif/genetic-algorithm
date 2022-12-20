package hust.pso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class PSO {
    public final static int PARTICLE_COUNT = 100; // số lượng cá thể
    public final static int V_MAX = 3; // tốc độ tối đa cho phép thay đổi
    // Range: 0 < V_MAX < CITY_COUNT
    public final static int MAX_ITERATION = 100;
    ArrayList<Particle> particles = new ArrayList<Particle>();
    ArrayList<City> map = new ArrayList<City>();
    int CITY_COUNT;
    ArrayList<Integer> XLocs = new ArrayList<>();
    ArrayList<Integer> YLocs = new ArrayList<>();

    public PSO() {
    }

    public void intializeMap() {
        for (int i = 0; i < CITY_COUNT; i++) {
            int x = XLocs.get(i);
            int y = YLocs.get(i);

            City city = new City(x, y);
            map.add(city);
        }
    }

    /**
     * Khởi tạo tuyến đường cho các cá thế
     */
    public void intializeRoute() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            Particle particle = new Particle(CITY_COUNT);
            particle.setName(i);

            for (int j = 0; j < CITY_COUNT; j++) {
                particle.data(j, j); // cập nhật vị trí của cá thành phố cho mỗi cá thế
                // sau này sẽ là tuyến đường của cá thế đó
            }
            particles.add(particle);

            for (int j = 0; j < CITY_COUNT; j++) {
                randomlyArrange(particles.indexOf(particle));
            }
            getTotalDistance(particles.indexOf(particle));
        }
    }

    public void PSOAlgorithm() {
        Particle particle = null;
        int iteration = 0;
        boolean done = false;

        intializeRoute();

        while (!done) {
            if (iteration < MAX_ITERATION) {
                for (int i = 0; i < PARTICLE_COUNT; i++) {
//                    particle = particles.get(i);
                    // cập nhật pBest
                    getTotalDistance(i);
                }
                // sắp xếp lại vị trí của các cá thể, cá thể có vị trí tốt nhất sẽ lên đầu
                Collections.sort(particles);
                System.out.println("best: " + particles.get(0).getLocalBest());
                updateVelocity();
                updateParticles();

                System.out.println("iteration number: " + iteration);
                iteration++;
            } else {
                done = true;
            }
        }
    }

    /**
     * Cập nhật vận tốc cho cá thế
     */
    public void updateVelocity() {
        double worstResults = 0;
        double vValue = 0.0;

        worstResults = particles.get(PARTICLE_COUNT - 1).getLocalBest();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            vValue = (V_MAX * particles.get(i).getLocalBest()) / worstResults;
            // cá thể có vị trí càng tồi thì sẽ phải đi càng nhiều

            if (vValue > V_MAX) {
                particles.get(i).setVelocity(V_MAX);
            } else if (vValue < 0.0) {
                particles.get(i).setVelocity(0.0);
            } else {
                particles.get(i).setVelocity(vValue);
            }
        }
    }

    public void updateParticles() {
        // Cá thể tốt nhất đã ở vị trí đầu tiên nên sẽ bắt đầu từ cá thể thứ 2
        for (int i = 1; i < PARTICLE_COUNT; i++) {
            // vận tốc càng cao -> phải thay đổi càng nhiều
            int changes = (int) Math.floor(Math.abs(particles.get(i).getVelocity()));
//            System.out.println("Changes for particle " + i + ": " + changes);
            for (int j = 0; j < changes; j++) {
                if (new Random().nextBoolean()) {
                    randomlyArrange(i);
                }
                // đưa cá thể đó lại gần với trị ví tốt nhất.
                improveParticle(0, i);
            } // j
            // Update pBest value.
//            getTotalDistance(i);
        } // i
    }

    public void improveParticle(int src, int des) {
        Particle best = particles.get(src);

        // lấy ngẫu nhiên 2 thành phố trong tuyến đường tốt nhất
        int targetA = new Random().nextInt(CITY_COUNT); // lựa chọn ngẫu nhiên 1 thành phố trong best
        int targetB = 0; // thành phố tiếp theo đc di từ targetA
        for (int i = 0; i < CITY_COUNT; i++) {
            if (best.data(i) == targetA) {
                if (i == CITY_COUNT - 1) {
                    targetB = best.data(0);
                } else {
                    targetB = best.data(i + 1);
                }
            }
        }

        Particle currentParticle = particles.get(des);
        int indexA = 0; // vị trí tương ứng của thành phố A và B trong current với best
        int indexB = 0;
        for (int i = 0; i < CITY_COUNT; i++) {
            if (currentParticle.data(i) == targetA) {
                indexA = i;
            }
            if (currentParticle.data(i) == targetB) {
                indexB = i;
            }
        }

        int tmpIndex = 0; // vị trí mới của thành phố B
        if (indexA == CITY_COUNT - 1) {
            tmpIndex = 0;
        } else {
            tmpIndex = indexA + 1;
        }

        // đẩy thành phố B lại gần thành phố A
        int tmp = currentParticle.data(tmpIndex);
        currentParticle.data(tmpIndex, currentParticle.data(indexB));
        currentParticle.data(indexB, tmp);


    }


    /**
     * Hàm tạo tuyến đường ngẫu nhiên cho các cá thế
     *
     * @param index
     */
    private void randomlyArrange(int index) {
        int cityA = new Random().nextInt(CITY_COUNT);
        int cityB = 0;
        boolean done = false;
        while (!done) {
            cityB = new Random().nextInt(CITY_COUNT);
            if (cityB != cityA) {
                done = true;
            }
        }
        int temp = particles.get(index).data(cityA);
        particles.get(index).data(cityA, particles.get(index).data(cityB));
        particles.get(index).data(cityB, temp);
    }

    /**
     * cập nhật vị trí tốt nhất cho cá thế
     * @param index
     */
    public void getTotalDistance(int index) {
        Particle particle = particles.get(index);
        particle.setLocalBest(0.0);

        double distance = 0.0;
        for (int i = 0; i < CITY_COUNT; i++) {
            if (i == CITY_COUNT - 1) {
                distance += distanceTo(particle.data(CITY_COUNT - 1), particle.data(0));
                particle.setLocalBest(distance);
            } else {
                distance += distanceTo(particle.data(i), particle.data(i + 1));
            }
        }
    }

    /**
     * Lấy khoảng cách giữa 2 thành phố
     * @param firstCity
     * @param secondCity
     * @return
     */
    public double distanceTo(int firstCity, int secondCity) {
        City cityA = null;
        City cityB = null;
        cityA = map.get(firstCity);
        cityB = map.get(secondCity);
        return cityA.distanceToCity(cityB);
    }

    public void printBestSolution() {
        System.out.print("Shortest Route: ");
        for (int j = 0; j < CITY_COUNT; j++) {
            System.out.print(particles.get(0).data(j) + ", ");
        } // j

        System.out.print("Distance: " + particles.get(0).getLocalBest() + "\n");

    }

    /**
     * Lấy danh sách các thành phố
     *
     * @param path
     */
    public void getCities(String path) {
        File file = new File(path);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int i = 0;
        while (scanner.hasNextLine()) {
            int name = scanner.nextInt();
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            XLocs.add(x);
            YLocs.add(y);

            System.out.println(x + " " + y);
            i++;
        }

        this.CITY_COUNT = i;
    }


}
