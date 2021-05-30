package src.controller;

import processing.core.PApplet;
import src.entities.Food;
import src.entities.Snake;
import src.entities.GameBoard;
import src.utils.Position;

public class GameController {
    private PApplet sketch;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;

    private static final int TILE_SIZE = 10;
    private static final int SNAKE_LENGTH = 4;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
    }

    public void resetGame() {
        createGameBoard();
        createComponentsForGameBoard();
        addComponentsToGameBoard();
    }

    public void createGameBoard() {
        this.gameBoard = new GameBoard(this.sketch, this.sketch.width, this.sketch.height, TILE_SIZE);
        this.gameBoard.setColor(0, 220, 0, 220);
    }

    public void createComponentsForGameBoard() {
        createSnake();
        createFood();
    }

    public void addComponentsToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.food);
        this.gameBoard.addDrawableGameComponent(this.snake);
    }

    private void createFood() {
        this.food = new Food(this.sketch, 2 * TILE_SIZE);
        this.food.setPosition(
                Position.getRandomPosition(this.gameBoard.nCols, this.gameBoard.nRows).scale(TILE_SIZE, TILE_SIZE));
        this.food.setColor(220, 0, 0, 220);
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, 2 * TILE_SIZE);
        this.snake.createBody();
        this.snake.setPosition(
                new Position(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2).scale(TILE_SIZE, TILE_SIZE));
        this.snake.setColor(0, 0, 220, 220);
    }

    public void renderGameBoard() {
        this.gameBoard.render();
    }

}