package org.example;

import java.awt.*;
import java.util.Arrays;

public abstract class Shape implements Drawable {
    protected Point[] points;
    protected Color color;
    protected int currentRotation;

    public Shape(Point[] points, Color color) {
        this.points = points;
        this.color = color;
        this.currentRotation = 0;
    }
    public Color getColor() {
        return color;
    }

    public abstract void rotate();
    public abstract void rotateBack();

    public void moveLeft() {
        Arrays.stream(points).forEach(p -> p.x--);
    }

    public void moveRight() {
        Arrays.stream(points).forEach(p -> p.x++);
    }

    public void moveDown() {
        Arrays.stream(points).forEach(p -> p.y++);
    }

    public Point[] getPoints() {
        return points;
    }

    @Override
    public void draw(Graphics g, int blockSize) {
        g.setColor(color);
        for (Point p : points) {
            g.fill3DRect(p.x * blockSize, p.y * blockSize,
                    blockSize - 1, blockSize - 1, true);
        }
    }
}