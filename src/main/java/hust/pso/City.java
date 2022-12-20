package hust.pso;

public class City {
    private int x;
    private int y;

    public City(int x, int y) {
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

    @Override
    public boolean equals(Object object) {
        hust.pso.City city = (hust.pso.City) object;
        boolean check = (x == city.getX()) && (y == city.getY());
        if (check) {
            return  true;
        }
        return false;
    }
}
