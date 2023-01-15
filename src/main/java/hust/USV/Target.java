package hust.USV;

public class Target implements Cloneable {
    public String name;
    public int x;
    public int y;
    public int w; // trọng số của mục tieu

    public Target(String name, int x, int y, int w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    /**
     * Khoảng cách giữa 2 mục tiêu
     */
    public double distance(Target target) {
        return Math.sqrt(Math.pow(x - target.x, 2) + Math.pow(y - target.y, 2));
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }
}
