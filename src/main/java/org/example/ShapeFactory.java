package org.example;

import java.awt.*;
import java.util.Random;

public class ShapeFactory {
    private static final int[][][] SHAPES = {
            {{0,0}, {1,0}, {2,0}, {3,0}},  // I
            {{0,0}, {0,1}, {1,0}, {1,1}},  // O
            {{0,0}, {1,0}, {1,1}, {2,1}},  // Z
            {{0,1}, {1,1}, {1,0}, {2,0}},  // S
            {{0,0}, {1,0}, {2,0}, {1,1}},  // T
            {{0,0}, {1,0}, {2,0}, {2,1}},  // L
            {{0,0}, {1,0}, {2,0}, {0,1}}   // J
    };

    private static final Color[] COLORS = {
            Color.CYAN, Color.YELLOW, Color.RED, Color.GREEN,
            Color.MAGENTA, Color.ORANGE, Color.BLUE
    };

    public static Shape createRandomShape(int boardWidth) {
        try {
            Random random = new Random();
            int index = random.nextInt(SHAPES.length);
            Point[] points = new Point[4];
            for (int i = 0; i < 4; i++) {
                points[i] = new Point(
                        SHAPES[index][i][0] + boardWidth/2,
                        SHAPES[index][i][1]
                );
            }
            return new TetrisShape(points, COLORS[index]);
        } catch (Exception e) {
            throw new GameInitializationException("Failed to create shape", e);
        }
    }
}