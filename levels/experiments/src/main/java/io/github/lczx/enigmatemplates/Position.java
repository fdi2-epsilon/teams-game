package io.github.lczx.enigmatemplates;

public class Position {

    public final long x;
    public final long y;

    public Position(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }

    public static Position fromXmlString(String str) {
        String[] coordsStr = str.split(" ");
        return new Position(Long.parseLong(coordsStr[0]), Long.parseLong(coordsStr[1]));
    }

}