package src.controller;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.entities.Food;
import src.entities.Snake;
import src.evt.EventHandler;
import src.evt.State;
import src.entities.GameBoard;
import src.utils.Position;

public class GameController {
    private boolean isGameRunning;
    private PApplet sketch;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;

    private EventHandler eventHandler;

    // lower = faster
    private static final int SPEED = 10;
    private static final int TILE_SIZE = 10;

    private static final int SNAKE_LENGTH = 4;
    private static final int SNAKE_BODY_PART_SIZE = TILE_SIZE;

    private static final int FOOD_SIZE = TILE_SIZE;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
        this.eventHandler = new EventHandler();
        this.isGameRunning = true;
    }

    public void resetGame() {
        createGameBoard();
        createAndAddComponentsToGameBoard();
        attachEventListeners();
    }

    public void createGameBoard() {
        this.gameBoard = new GameBoard(this.sketch, this.sketch.height / TILE_SIZE, this.sketch.width / TILE_SIZE,
                TILE_SIZE);
        this.gameBoard.setColor(0, 220, 0, 220);
    }

    private void createAndAddComponentsToGameBoard() {
        createSnake();
        addSnakeToGameBoard();
        createFood();
        addFoodToGameBoard();
    }

    public void attachEventListeners() {
        this.eventHandler.addListener(this.snake);
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, SNAKE_BODY_PART_SIZE);
        this.snake.createBody();
        this.snake.setPosition(Position.getRandomPosition(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2)
                .scale(TILE_SIZE, TILE_SIZE));
        this.snake.setColor(0, 0, 220, 220);
    }

    private void addSnakeToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.snake);
        for (DrawableGameComponent bodyPart : this.snake.body) {
            this.gameBoard.fillPosition(bodyPart.getPosition());
        }
    }

    private void createFood() {
        this.food = new Food(this.sketch, FOOD_SIZE);
        this.food.setPosition(this.gameBoard.getRandomVacantPosition());
        this.food.setColor(220, 0, 0, 220);
    }

    private void addFoodToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.food);
        this.gameBoard.fillPosition(this.food.getPosition());
    }

    public void updateGameBoard() {
        if (this.sketch.frameCount % SPEED == 0 && shouldKeepRunning()) {
            this.gameBoard.update();
            updateTileStates();
        }
    }

    private boolean shouldKeepRunning() {
        if (this.gameBoard.getNumVacantTiles() == 0 || this.snake.hasSnakeHitEdge() || this.snake.hasEatenItself()) {
            this.isGameRunning = false;
        }
        return this.isGameRunning;
    }

    private void updateTileStates() {
        Position toFree = this.snake.getPrevTailPosition();
        Position snakePosition = this.snake.getPosition();
        this.gameBoard.vacatePosition(toFree);
        this.gameBoard.fillPosition(snakePosition);
        Position foodPosition = this.food.getPosition();
        if (snakePosition.equals(foodPosition)) {
            this.snake.eatFood();
            this.food.setPosition(this.gameBoard.getRandomVacantPosition());
            this.gameBoard.fillPosition(this.food.getPosition());
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