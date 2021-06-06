package src.controller;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.entities.Food;
import src.entities.Snake;
import src.evt.EventHandler;
import src.evt.State;
import src.fsm.GameState;
import src.entities.GameBoard;
import src.entities.Score;
import src.utils.Position;

public class GameController {
    private PApplet sketch;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;
    private Score scoreBoard;

    public EventHandler eventHandler;

    private static final int SPEED = 10;
    private static float SNAKE_SPEED;
    private static final int TILE_SIZE = 10;

    private static final int SNAKE_LENGTH = 4;
    private static final int SNAKE_BODY_PART_SIZE = TILE_SIZE;

    private static final int FOOD_SIZE = TILE_SIZE;

    private static final int TEXT_SIZE = 20;

    // modes and states
    public GameState gameState;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
        SNAKE_SPEED = this.sketch.frameRate / SPEED;
        this.eventHandler = new EventHandler();
    }

    public void resetGame() {
        createGameBoard();
        createAndAddComponentsToGameBoard();
        setupEventListeners();
        startGame();
    }

    private void createGameBoard() {
        this.gameBoard = new GameBoard(this.sketch, this.sketch.height / TILE_SIZE, this.sketch.width / TILE_SIZE,
                TILE_SIZE);
        this.gameBoard.setColor(0, 220, 0, 220);
    }

    private void createAndAddComponentsToGameBoard() {
        createSnake();
        addSnakeToGameBoard();
        createFood();
        addFoodToGameBoard();
        createScoreBoard();
        addScoreBoardToGameBoard();
    }

    private void setupEventListeners() {
        this.eventHandler.reset();
        this.eventHandler.addListener(this.snake);
        this.eventHandler.addListener(this.gameBoard);
        this.eventHandler.addListener(this.scoreBoard);
    }

    private void startGame() {
        this.gameState = GameState.runningState;
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, SNAKE_BODY_PART_SIZE);
        this.snake.createBody();
        this.snake.setPosition(
                new Position(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2).scale(TILE_SIZE, TILE_SIZE));
        this.snake.setColor(0, 200, 0, 220);
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
        this.food.setColor(0, 0, 0, 220);
    }

    private void addFoodToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.food);
        this.gameBoard.fillPosition(this.food.getPosition());
    }

    private void createScoreBoard() {
        this.scoreBoard = new Score(this.sketch, TEXT_SIZE);
        this.scoreBoard.setPosition(10, 10 + TEXT_SIZE);
        this.scoreBoard.setColor(10, 10, 10, 50);
    }

    private void addScoreBoardToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.scoreBoard);
    }

    public boolean hasGameEnded() {
        return this.gameBoard.getNumVacantTiles() == 0 || this.snake.hasSnakeHitEdge() || this.snake.hasEatenItself();
    }

    public void updateGameBoard() {
        this.gameBoard.update();
    }

    public void updateGame() {
        if (this.sketch.frameCount % SNAKE_SPEED == 0) {
            this.gameState.update(this);
        }
    }

    public void updateTileStates() {
        Position toFree = this.snake.getPrevTailPosition();
        Position snakePosition = this.snake.getPosition();
        this.gameBoard.vacatePosition(toFree);
        this.gameBoard.fillPosition(snakePosition);
        Position foodPosition = this.food.getPosition();
        if (snakePosition.equals(foodPosition)) {
            this.snake.eatFood();
            this.eventHandler.setEventState(State.FOOD_EATEN);
            this.eventHandler.handleEvent();
            this.food.setPosition(this.gameBoard.getRandomVacantPosition());
            this.gameBoard.fillPosition(this.food.getPosition());
        }
    }

    public void renderGame() {
        this.gameBoard.render();
    }

    public void keyPressed() {
        this.gameState.handleInput(this, this.sketch.keyCode);
    }

}