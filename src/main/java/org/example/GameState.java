package org.example;

public class GameState {
    private int score;
    private int level;
    private int lines;
    private boolean isGameOver;

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLines() { return lines; }
    public boolean isGameOver() { return isGameOver; }
    public void setGameOver(boolean gameOver) { isGameOver = gameOver; }

    public GameState() {
        this.score = 0;
        this.level = 1;
        this.lines = 0;
        this.isGameOver = false;
    }

    public void addLines(int linesCleared) {
        this.lines += linesCleared;
        this.score += calculateScore(linesCleared);
        this.level = 1 + this.lines / 10;
    }

    private int calculateScore(int linesCleared) {
        return switch (linesCleared) {
            case 1 -> 100 * level;
            case 2 -> 300 * level;
            case 3 -> 500 * level;
            case 4 -> 800 * level;
            default -> 0;
        };
    }

    public void reset() {
        this.score = 0;
        this.level = 1;
        this.lines = 0;
        this.isGameOver = false;
    }


}
