# Tetris with Non-Conventional Shapes

This is a Tetris game implementation that includes both the classic Tetris shapes and new, non-conventional shapes. The project features core game mechanics, such as shape rotation, score tracking, and game over conditions.

## Features

- **Classic Tetris Shapes**: I, O, Z, S, T, L, J.
- **Non-Conventional Shapes**: New shapes like Plus Sign, Diagonal Line, U Shape, Backward L, Cross, and Snake.
- **Game State Management**: Tracks score, level, lines cleared, and game over status.
- **Random Shape Generation**: New shapes are randomly generated on the game board.
- **Shape Rotation**: Tetriminos can rotate in place.

## Project Structure

- `Drawable.java`: Interface for drawing objects on the screen.
- `GameInitializationException.java`: Custom exception for game initialization errors.
- `GameState.java`: Handles the game state, including score, level, lines, and game-over conditions.
- `GameBoard.java`: Represents the game board, including methods for checking and setting cells.
- `ShapeFactory.java`: Creates random Tetris shapes, including both classic and non-conventional ones.
- `TetrisShape.java`: A subclass of `Shape`, specifically for Tetris shapes with rotation methods.

## How to Play

1. Start the game by running the main application.
2. Shapes will fall from the top of the screen.
3. Use arrow keys to move or rotate shapes.
4. Clear lines by filling them completely with shapes.
5. The game ends when there is no space for a new shape.

## Requirements

- Java 8 or higher.
- A graphics library (e.g., AWT or Swing) to render the game.
