package hust.USV.kmean;

public class Centroid {
    int x;
    int y;

    public Centroid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Centroid centroid){
        return x == centroid.x && y == centroid.y;
    }
}
