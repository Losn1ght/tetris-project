package org.example;

import java.awt.*;

public class TetrisShape extends Shape {
    public TetrisShape(Point[] points, Color color) {
        super(points, color);
    }

    @Override
    public void rotate() {
        currentRotation = (currentRotation + 1) % 4;
        Point center = points[1];
        for (int i = 0; i < points.length; i++) {
            if (i != 1) {
                int x = points[i].x - center.x;
                int y = points[i].y - center.y;
                points[i].x = center.x - y;
                points[i].y = center.y + x;
            }
        }
    }

    @Override
    public void rotateBack() {
        currentRotation = (currentRotation + 3) % 4;
        Point center = points[1];
        for (int i = 0; i < points.length; i++) {
            if (i != 1) {
                int x = points[i].x - center.x;
                int y = points[i].y - center.y;
                points[i].x = center.x + y;
                points[i].y = center.y - x;
            }
        }
    }
}