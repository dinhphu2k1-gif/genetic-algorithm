package hust.aco.tsp;

public class City {
    private String name;
    private int x;
    private int y;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public double distanceToCity(City city) {
        int x = Math.abs(this.x - city.getX());
        int y = Math.abs(this.y - city.getY());

        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString(){ return name;}
}
