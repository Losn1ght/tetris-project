package org.example;

import java.awt.*;
import java.util.Random;

public class ShapeFactory {
    private static final int[][][] SHAPES = {
            // Original shapes
            {{0,0}, {1,0}, {2,0}, {3,0}},  // I
            {{0,0}, {0,1}, {1,0}, {1,1}},  // O
            {{0,0}, {1,0}, {1,1}, {2,1}},  // Z
            {{0,1}, {1,1}, {1,0}, {2,0}},  // S
            {{0,0}, {1,0}, {2,0}, {1,1}},  // T
            {{0,0}, {1,0}, {2,0}, {2,1}},  // L
            {{0,0}, {1,0}, {2,0}, {0,1}},  // J

            // New non-conventional shapes
            {{1,0}, {0,1}, {1,1}, {2,1}, {1,2}},  // Plus Sign (5 blocks)
            {{0,0}, {1,1}, {2,2}, {3,3}},         // Diagonal Line
            {{0,0}, {0,1}, {0,2}, {1,0}, {2,0}},  // U Shape
            {{0,0}, {1,0}, {1,1}, {1,2}},         // Backward L
            {{0,0}, {1,0}, {2,0}, {1,1}, {1,2}},  // Cross
            {{0,0}, {0,1}, {1,1}, {2,1}, {2,2}}   // Snake
    };

    private static final Color[] COLORS = {
            // Original colors
            Color.CYAN, Color.YELLOW, Color.RED, Color.GREEN,
            Color.MAGENTA, Color.ORANGE, Color.BLUE,

            // New colors for new shapes
            new Color(128, 0, 128),    // Purple
            new Color(255, 192, 203),  // Pink
            new Color(165, 42, 42),    // Brown
            new Color(64, 224, 208),   // Turquoise
            new Color(255, 215, 0),    // Gold
            new Color(219, 112, 147)   // PaleVioletRed
    };

    public static Shape createRandomShape(int boardWidth) {
        try {
            Random random = new Random();
            int index = random.nextInt(SHAPES.length);

            // Calculate number of blocks in the shape
            int numBlocks = SHAPES[index].length;
            Point[] points = new Point[numBlocks];

            // Calculate starting position based on shape width
            int shapeWidth = calculateShapeWidth(SHAPES[index]);
            int startX = (boardWidth - shapeWidth) / 2;

            for (int i = 0; i < numBlocks; i++) {
                points[i] = new Point(
                        SHAPES[index][i][0] + startX,
                        SHAPES[index][i][1]
                );
            }
            return new TetrisShape(points, COLORS[index]);
        } catch (Exception e) {
            throw new GameInitializationException("Failed to create shape", e);
        }
    }

    private static int calculateShapeWidth(int[][] shape) {
        int maxX = 0;
        for (int[] point : shape) {
            maxX = Math.max(maxX, point[0]);
        }
        return maxX + 1;
    }
}