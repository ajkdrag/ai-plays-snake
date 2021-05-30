package src.controller;


import processing.core.PApplet;
import src.entities.Food;
import src.entities.Snake;
import src.evt.EventHandler;
import src.evt.State;
import src.entities.GameBoard;
import src.utils.Position;

public class GameController {
    private PApplet sketch;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;

    private EventHandler eventHandler;

    private static final int SPEED = 6;
    private static final int TILE_SIZE = 10;

    private static final int SNAKE_LENGTH = 4;
    private static final int SNAKE_BODY_PART_SIZE = TILE_SIZE * 2;

    private static final int FOOD_SIZE = TILE_SIZE * 2;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
        this.eventHandler = new EventHandler();
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
        this.food = new Food(this.sketch, FOOD_SIZE);
        this.food.setPosition(
                Position.getRandomPosition(this.gameBoard.nCols, this.gameBoard.nRows).scale(TILE_SIZE, TILE_SIZE));
        this.food.setColor(220, 0, 0, 220);
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, SNAKE_BODY_PART_SIZE);
        this.snake.createBody();
        this.snake.setPosition(
                new Position(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2).scale(TILE_SIZE, TILE_SIZE));
        this.snake.setColor(0, 0, 220, 220);
        this.eventHandler.addListener(this.snake);
    }

    public void updateGameBoard() {
        if (this.sketch.frameCount % SPEED == 0) {
            this.gameBoard.update();
            // control snake
            controlSnakeIfCrossedEdge();
        }
    }

    private void controlSnakeIfCrossedEdge() {
        Position snakePosition = this.snake.getPosition();
        if (snakePosition.x < 0) {
            this.snake.setHeadPosition(this.sketch.width - SNAKE_BODY_PART_SIZE, snakePosition.y);
            return;
        }
        if (snakePosition.x >= this.sketch.width) {
            this.snake.setHeadPosition(0, snakePosition.y);
            return;
        }
        if (snakePosition.y < 0) {
            this.snake.setHeadPosition(snakePosition.x, this.sketch.height - SNAKE_BODY_PART_SIZE);
            return;
        }
        if (snakePosition.y >= this.sketch.height) {
            this.snake.setHeadPosition(snakePosition.x, 0);
            return;
        }
    }

    public void renderGameBoard() {
        this.gameBoard.render();
    }

    public void keyPressed() {
        if (this.sketch.keyCode == PApplet.UP) {
            this.eventHandler.setEventState(State.KEY_PRESSED_UP);
        } else if (this.sketch.keyCode == PApplet.DOWN) {
            this.eventHandler.setEventState(State.KEY_PRESSED_DOWN);
        } else if (this.sketch.keyCode == PApplet.LEFT) {
            this.eventHandler.setEventState(State.KEY_PRESSED_LEFT);
        } else if (this.sketch.keyCode == PApplet.RIGHT) {
            this.eventHandler.setEventState(State.KEY_PRESSED_RIGHT);
        } else {
            this.eventHandler.setEventState(State.KEY_INVALID);
        }
        this.eventHandler.handleEvent();
    }

}