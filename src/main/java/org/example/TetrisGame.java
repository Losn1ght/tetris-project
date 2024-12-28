package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisGame extends JFrame {
    private static final int BLOCK_SIZE = 25;
    private static final int SIDE_PANEL_WIDTH = 200;

    private static final Font TITLE_FONT = new Font("Consolas", Font.BOLD, 24);
    private static final Font STATS_FONT = new Font("Consolas", Font.BOLD, 20);
    private static final Font CONTROLS_FONT = new Font("Consolas", Font.PLAIN, 16);
    private static final Font MESSAGE_FONT = new Font("Consolas", Font.BOLD, 30);

    private final GameBoard board;
    private Shape currentShape;
    private Timer gameTimer;
    private final GameState gameState;
    private final Color[][] blockColors;
    private boolean isPaused;
    private JButton restartButton;
    private JButton pauseButton;

    public TetrisGame() {
        try {
            this.board = new GameBoard(15, 20);
            this.gameState = new GameState();
            this.currentShape = ShapeFactory.createRandomShape(board.getWidth());
            this.blockColors = new Color[board.getHeight()][board.getWidth()];
            this.isPaused = false;

            initializeUI();
            initializeGameTimer();
            setFocusable(true);
        } catch (Exception e) {
            throw new GameInitializationException("Failed to initialize game", e);
        }
    }

    private void initializeUI() {
        setTitle("Tetris");
        setSize(board.getWidth() * BLOCK_SIZE + SIDE_PANEL_WIDTH,
                board.getHeight() * BLOCK_SIZE + 40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(createGamePanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
        addKeyListener(new TetrisKeyListener());
    }

    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(
                board.getWidth() * BLOCK_SIZE,
                board.getHeight() * BLOCK_SIZE
        ));
        gamePanel.setBackground(new Color(20, 20, 20));
        return gamePanel;
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(
                SIDE_PANEL_WIDTH,
                board.getHeight() * BLOCK_SIZE
        ));
        sidePanel.setBackground(new Color(240, 240, 240));

        // Add stats panel
        JPanel statsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawStats(g);
            }
        };
        statsPanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 250));
        statsPanel.setBackground(new Color(240, 240, 240));
        sidePanel.add(statsPanel);

        // Add some rigid space before the button panel
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Set consistent button dimensions
        Dimension buttonSize = new Dimension(100, 30);

        // Create and configure pause button
        pauseButton = new JButton("Pause");
        pauseButton.setPreferredSize(buttonSize);
        pauseButton.setMinimumSize(buttonSize);
        pauseButton.setMaximumSize(buttonSize);
        pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseButton.addActionListener(e -> togglePause());
        pauseButton.setFocusable(false);
        buttonPanel.add(pauseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create and configure restart button
        restartButton = new JButton("Restart");
        restartButton.setPreferredSize(buttonSize);
        restartButton.setMinimumSize(buttonSize);
        restartButton.setMaximumSize(buttonSize);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setFocusable(false);
        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        sidePanel.add(buttonPanel);
        return sidePanel;
    }

    private void drawStats(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(STATS_FONT);

        // Draw score
        g.drawString("Score: " + gameState.getScore(), 20, 30);

        // Draw level
        g.drawString("Level: " + gameState.getLevel(), 20, 60);

        // Draw lines cleared
        g.drawString("Lines: " + gameState.getLines(), 20, 90);

        // Draw controls
        g.setFont(CONTROLS_FONT);
        String[] controls = {
                "Controls:",
                "←: Move Left",
                "→: Move Right",
                "↓: Move Down",
                "↑: Rotate",
                "Space: Drop",
                "P: Pause"
        };

        for (int i = 0; i < controls.length; i++) {
            g.drawString(controls[i], 20, 150 + i * 25);
        }
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseButton.setText(isPaused ? "Resume" : "Pause");
        if (isPaused) {
            gameTimer.stop();
        } else {
            gameTimer.start();
        }
        repaint();
    }

    private void restartGame() {
        // Reset game state
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                board.setCell(col, row, false);
                blockColors[row][col] = null;
            }
        }

        // Reset game properties
        isPaused = false;
        pauseButton.setText("Pause");
        gameState.reset(); // Add reset() method to GameState class
        currentShape = ShapeFactory.createRandomShape(board.getWidth());
        gameTimer.setDelay(1000);
        gameTimer.start();

        // Ensure focus returns to the game window
        this.requestFocus();
        repaint();
    }

    private boolean canMove(int deltaX, int deltaY) {
        for (Point p : currentShape.getPoints()) {
            int newX = p.x + deltaX;
            int newY = p.y + deltaY;

            // Check boundaries
            if (newX < 0 || newX >= board.getWidth() ||
                    newY >= board.getHeight()) {
                return false;
            }

            // Check collision with placed blocks
            if (newY >= 0 && board.isOccupied(newX, newY)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPosition() {
        for (Point p : currentShape.getPoints()) {
            if (p.x < 0 || p.x >= board.getWidth() ||
                    p.y >= board.getHeight() ||
                    (p.y >= 0 && board.isOccupied(p.x, p.y))) {
                return false;
            }
        }
        return true;
    }

    private void clearLines() {
        int linesCleared = 0;
        boolean[][] cells = board.getCells();

        for (int row = board.getHeight() - 1; row >= 0; row--) {
            if (isLineFull(row)) {
                removeLine(row);
                linesCleared++;
                row++; // Check the same row again after shifting
            }
        }

        if (linesCleared > 0) {
            gameState.addLines(linesCleared);
            updateGameSpeed();
        }
    }

    private boolean isLineFull(int row) {
        for (int col = 0; col < board.getWidth(); col++) {
            if (!board.isOccupied(col, row)) {
                return false;
            }
        }
        return true;
    }

    private void updateGameSpeed() {
        int delay = Math.max(100, 1000 - (gameState.getLevel() - 1) * 50);
        gameTimer.setDelay(delay);
    }

    private void handleGameError(Exception e) {
        JOptionPane.showMessageDialog(this,
                "An error occurred: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        gameState.setGameOver(true);
    }

    private void initializeGameTimer() {
        gameTimer = new Timer(1000, e -> {
            try {
                if (!gameState.isGameOver() && !isPaused) {
                    if (canMove(0, 1)) {
                        currentShape.moveDown();
                    } else {
                        placeShape();
                        clearLines();
                        currentShape = ShapeFactory.createRandomShape(board.getWidth());
                        if (!isValidPosition()) {
                            gameState.setGameOver(true);
                        }
                    }
                    repaint();
                }
            } catch (Exception ex) {
                handleGameError(ex);
            }
        });
        gameTimer.start();
    }

    private void drawGame(Graphics g) {
        // Draw board grid and placed blocks
        g.setColor(Color.DARK_GRAY);
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE,
                        BLOCK_SIZE, BLOCK_SIZE);
                if (board.isOccupied(col, row)) {
                    g.setColor(blockColors[row][col]);
                    g.fill3DRect(col * BLOCK_SIZE, row * BLOCK_SIZE,
                            BLOCK_SIZE - 1, BLOCK_SIZE - 1, true);
                    g.setColor(Color.DARK_GRAY);
                }
            }
        }

        // Draw current shape
        if (currentShape != null && !gameState.isGameOver() && !isPaused) {
            currentShape.draw(g, BLOCK_SIZE);
        }

        // Draw pause overlay
        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(0, 0, board.getWidth() * BLOCK_SIZE,
                    board.getHeight() * BLOCK_SIZE);
            g.setColor(Color.WHITE);
            g.setFont(MESSAGE_FONT);
            String pausedText = "PAUSED";
            FontMetrics metrics = g.getFontMetrics();
            int x = (board.getWidth() * BLOCK_SIZE - metrics.stringWidth(pausedText)) / 2;
            int y = (board.getHeight() * BLOCK_SIZE) / 2;
            g.drawString(pausedText, x, y);
        }

        // Draw game over message
        if (gameState.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(0, 0, board.getWidth() * BLOCK_SIZE,
                    board.getHeight() * BLOCK_SIZE);
            g.setColor(Color.RED);
            g.setFont(MESSAGE_FONT);
            String gameOver = "Game Over!";
            FontMetrics metrics = g.getFontMetrics();
            int x = (board.getWidth() * BLOCK_SIZE - metrics.stringWidth(gameOver)) / 2;
            int y = (board.getHeight() * BLOCK_SIZE) / 2;
            g.drawString(gameOver, x, y);
        }
    }

    private void placeShape() {
        for (Point p : currentShape.getPoints()) {
            if (p.y >= 0) {
                board.setCell(p.x, p.y, true);
                blockColors[p.y][p.x] = currentShape.getColor(); // Store the color
            }
        }
    }

    private void removeLine(int row) {
        // Shift all rows above down
        for (int r = row; r > 0; r--) {
            for (int col = 0; col < board.getWidth(); col++) {
                board.setCell(col, r, board.isOccupied(col, r - 1));
                blockColors[r][col] = blockColors[r - 1][col]; // Move colors down
            }
        }

        // Clear top row
        for (int col = 0; col < board.getWidth(); col++) {
            board.setCell(col, 0, false);
            blockColors[0][col] = null;
        }
    }

    private class TetrisKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if (gameState.isGameOver()) return;

            try {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (!isPaused && canMove(-1, 0)) currentShape.moveLeft();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (!isPaused && canMove(1, 0)) currentShape.moveRight();
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (!isPaused && canMove(0, 1)) currentShape.moveDown();
                    }
                    case KeyEvent.VK_UP -> {
                        if (!isPaused) {
                            currentShape.rotate();
                            if (!isValidPosition()) currentShape.rotateBack();
                        }
                    }
                    case KeyEvent.VK_SPACE -> {
                        if (!isPaused) {
                            while (canMove(0, 1)) {
                                currentShape.moveDown();
                                gameState.addLines(0);
                            }
                        }
                    }
                    case KeyEvent.VK_P -> togglePause();
                }
                repaint();
            } catch (Exception ex) {
                handleGameError(ex);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TetrisGame game = new TetrisGame();
            game.setVisible(true);
        });
    }
}