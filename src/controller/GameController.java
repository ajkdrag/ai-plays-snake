package src.controller;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.entities.Food;
import src.entities.Snake;
import src.evt.Event;
import src.evt.EventHandler;
import src.evt.EventListener;
import src.evt.State;
import src.entities.GameBoard;
import src.entities.Score;
import src.utils.Position;

public class GameController implements EventListener {
    private State gameState = State.GAME_STATE_ENDED;
    private State prevGameState;
    private PApplet sketch;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;
    private Score scoreBoard;

    private EventHandler eventHandler;

    // lower = faster
    private static final int SPEED = 6;
    private static final int TILE_SIZE = 10;

    private static final int SNAKE_LENGTH = 4;
    private static final int SNAKE_BODY_PART_SIZE = TILE_SIZE;

    private static final int FOOD_SIZE = TILE_SIZE;

    private static final int TEXT_SIZE = 20;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
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
        this.eventHandler.addListener(this);
    }

    private void startGame() {
        this.gameState = State.GAME_STATE_RUNNING;
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, SNAKE_BODY_PART_SIZE);
        this.snake.createBody();
        this.snake.setPosition(
                new Position(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2).scale(TILE_SIZE, TILE_SIZE));
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

    private void createScoreBoard() {
        this.scoreBoard = new Score(this.sketch, TEXT_SIZE);
        this.scoreBoard.setPosition(10, 10 + TEXT_SIZE);
        this.scoreBoard.setColor(10, 10, 10, 50);
    }

    private void addScoreBoardToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.scoreBoard);
    }

    public void updateGameBoard() {
        if (this.sketch.frameCount % SPEED == 0) {
            if (shouldUpdate()) {
                this.gameBoard.update();
                updateTileStates();
            }
            prevGameState = gameState;
        }
    }

    private boolean shouldUpdate() {
        if (this.gameState == State.GAME_STATE_RUNNING && this.gameState == this.prevGameState)
            return hasGameEnded();
        return false;
    }

    private boolean hasGameEnded() {
        if (this.gameBoard.getNumVacantTiles() == 0 || this.snake.hasSnakeHitEdge() || this.snake.hasEatenItself()) {
            this.gameState = State.GAME_STATE_ENDED;
        }
        return this.gameState == State.GAME_STATE_RUNNING;
    }

    private void updateTileStates() {
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

    public void renderGameBoard() {
        this.gameBoard.render();
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getState()) {
            case KEY_PRESSED_SHIFT:
                this.resetGame();
                break;
            case KEY_PRESSED_SPACE:
                if (this.gameState == State.GAME_STATE_RUNNING)
                    this.gameState = State.GAME_STATE_PAUSED;
                else if (this.gameState == State.GAME_STATE_PAUSED)
                    this.gameState = State.GAME_STATE_RUNNING;
                break;
            default:
                break;
        }
    }

    public void keyPressed() {
        switch (this.sketch.keyCode) {
            case PApplet.UP:
                this.eventHandler.setEventState(State.KEY_PRESSED_UP);
                break;
            case PApplet.DOWN:
                this.eventHandler.setEventState(State.KEY_PRESSED_DOWN);
                break;
            case PApplet.LEFT:
                this.eventHandler.setEventState(State.KEY_PRESSED_LEFT);
                break;
            case PApplet.RIGHT:
                this.eventHandler.setEventState(State.KEY_PRESSED_RIGHT);
                break;
            case PApplet.SHIFT:
                this.eventHandler.setEventState(State.KEY_PRESSED_SHIFT);
                break;
            case PApplet.TAB:
                this.eventHandler.setEventState(State.KEY_PRESSED_TAB);
                break;
            case ' ':
                this.eventHandler.setEventState(State.KEY_PRESSED_SPACE);
                break;
            default:
                this.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        this.eventHandler.handleEvent();
    }

}