package hust.aco;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Route {
//    public static ArrayList<City> initRoute = new ArrayList<>(Arrays.asList(
//            new City("Ha Noi", 100, 360),
//            new City("Thanh Hoa", 2, 3),
//            new City("Nam Dinh", 5, 7),
//            new City("Ninh Binh", 6, 2),
//            new City("Nghe An", -4, -3),
//            new City("Ha Tinh", -2, -1),
//            new City("Ha Long", 10, 6),
//            new City("Quang Ninh", 9, 8)
//    ));

    public static ArrayList<City> cities = initRoute();
    public static int numCities = cities.size();

    public static ArrayList<City> initRoute() {
        ArrayList<City> cityArrayList = new ArrayList<>();
        String path = "tsp/test_case/test_case10.txt";
        File file = new File(path);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int i = 0;
        while (scanner.hasNextLine()) {
            String name = String.valueOf(scanner.nextInt());
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            City city = new City(name, x, y);
            cityArrayList.add(city);
            System.out.println(x + " " + y);
            i++;
        }

        return cityArrayList;
    }

    public static double[][] initMatrix() {
        double[][] matrix = new double[numCities][numCities];

        for (int i = 0; i < numCities; i++) {
            City from = cities.get(i);
            for (int j = 0; j < numCities; j++) {
                City to = cities.get(j);
                double distance = from.distanceToCity(to);
                matrix[i][j] = distance;
            }
        }

        return matrix;
    }

}
