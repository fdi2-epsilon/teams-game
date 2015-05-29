package io.github.lczx.enigmatemplates;

public class ItemPlacer {

    // Canvas size in pixels
    private int canvasWidth, canvasHeight;

    // Canvas table dimensions
    private int rows, columns;

    // Item size in pixels
    private int itemWidth = 0, itemHeight = 0;

    // Canvas padding
    private int paddingX = 0, paddingY = 0;

    // Canvas placement offset
    private int offsetX = 0, offsetY = 0;

    // Generated position values
    private long[] xs = null;
    private long[] ys = null;

    // Are calculated values up-to date?
    boolean valid = false;

    public ItemPlacer(int canvasWidth, int canvasHeight, int numItems) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        // Assuming a square table
        int elemsPerLine = (int) Math.ceil(Math.sqrt(numItems));
        this.rows = elemsPerLine;
        this.columns = elemsPerLine;
    }

    public ItemPlacer(int canvasWidth, int canvasHeight, int rows, int columns) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.rows = rows;
        this.columns = columns;
    }

    public void setItemSize(int width, int height) {
        itemWidth = width;
        itemHeight = height;
        valid = false;
    }

    public void setPadding(int x, int y) {
        paddingX = x;
        paddingY = y;
        valid = false;
    }

    public void setOffset(int x, int y) {
        offsetX = x;
        offsetY = y;
        valid = false;
    }

    public Position getItemPosition(int index) {
        if (!valid)
            calculatePositions();

        int posX = index % columns;
        int posY = (int) Math.floor(index / rows);
        return new Position(xs[posX], ys[posY]);
    }


    private void calculatePositions() {
        int w = canvasWidth - 2 * paddingX;
        int h = canvasHeight - 2 * paddingY;

        xs = new long[columns];
        double stepX = (double) w / columns;
        double totOffsetsX = (double) paddingX + offsetX - itemWidth / 2;
        for (int i = 0; i < columns; ++i)
            xs[i] = Math.round(stepX * (.5 + i) + totOffsetsX);

        ys = new long[rows];
        double stepY = (double) h / rows;
        double totOffsetsY = (double) paddingY + offsetY - itemHeight / 2;
        for (int i = 0; i < rows; ++i)
            ys[i] = Math.round(stepY * (.5 + i) + totOffsetsY);

        valid = true;
    }

}
