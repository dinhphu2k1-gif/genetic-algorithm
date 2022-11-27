package hust.aco.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Route {
    public static ArrayList<City> initRoute = new ArrayList<>(Arrays.asList(
            new City("Ha Noi", 0, 0),
            new City("Thanh Hoa", 2, 3),
            new City("Nam Dinh", 5, 7),
            new City("Ninh Binh", 6, 2),
            new City("Nghe An", -4, -3),
            new City("Ha Tinh", -2, -1),
            new City("Ha Long", 10, 6),
            new City("Quang Ninh", 9, 8)
    ));

    public static int numCities = initRoute.size();

    public static double[][] initMatrix() {
        double[][] matrix = new double[numCities][numCities];

        for (int i = 0; i < numCities; i++) {
            City from = initRoute.get(i);
            for (int j = 0; j < numCities; j++) {
                City to = initRoute.get(j);
                double distance = from.distanceToCity(to);
                matrix[i][j] = distance;
            }
        }

        return matrix;
    }

}
