package org.example;

public class GameBoard {
    private final int width;
    private final int height;
    private final boolean[][] cells;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new boolean[height][width];
    }

    public boolean isOccupied(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width && cells[y][x];
    }

    public void setCell(int x, int y, boolean value) {
        if (y >= 0 && y < height && x >= 0 && x < width) {
            cells[y][x] = value;
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean[][] getCells() { return cells; }
}