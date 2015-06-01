package io.github.lczx.enigmatemplates;

public class Dimensions {

    private final long x;
    private final long y;

    public Dimensions(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }

    public static Dimensions fromXmlString(String str) {
        String[] coordsStr = str.split(" ");
        return new Dimensions(Long.parseLong(coordsStr[0]), Long.parseLong(coordsStr[1]));
    }

}
