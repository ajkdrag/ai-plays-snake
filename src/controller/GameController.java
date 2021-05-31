package src.controller;

import processing.core.PApplet;
import java.util.Random;
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
    private Random random;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;

    private int numVacantTiles;
    private int[] vacantList, vacantMap;

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
        this.random = new Random();
    }

    public void resetGame() {
        createGameBoard();
        createComponentsForGameBoard();
        cacheVacantTiles();
        addComponentsToGameBoard();
        attachEventListeners();
    }

    public void createGameBoard() {
        this.gameBoard = new GameBoard(this.sketch, this.sketch.width, this.sketch.height, TILE_SIZE);
        this.gameBoard.setColor(0, 220, 0, 220);
    }

    private void cacheVacantTiles() {
        this.vacantList = new int[this.gameBoard.nRows * this.gameBoard.nCols];
        this.vacantMap = new int[this.vacantList.length];
        this.numVacantTiles = this.vacantList.length;
        for (int i = 0, end = this.vacantList.length; i < end; ++i) {
            this.vacantList[i] = i;
            this.vacantMap[i] = i;
        }

        for (DrawableGameComponent bodyPart : this.snake.body) {
            fillPosition(bodyPart.getPosition());
        }
        fillPosition(this.food.getPosition());
    }

    private void fillPosition(Position position) {
        int relX = position.x / TILE_SIZE;
        int relY = position.y / TILE_SIZE;
        int tile = relY * this.gameBoard.nCols + relX;
        fillTile(tile);
    }

    private void vacatePosition(Position position) {
        int relX = position.x / TILE_SIZE;
        int relY = position.y / TILE_SIZE;
        int tile = relY * this.gameBoard.nCols + relX;
        vacateTile(tile);
    }

    private void vacateTile(int x) {
        if (vacantMap[x] != -1)
            return;
        vacantList[numVacantTiles] = x;
        vacantMap[x] = numVacantTiles;
        ++numVacantTiles;
    }

    private void fillTile(int x) {
        int positionInVacantList = vacantMap[x];
        if (positionInVacantList == -1)
            return;
        if (positionInVacantList == numVacantTiles - 1) {
            --numVacantTiles;
            vacantMap[x] = -1;
            return;
        }
        int lastVacantTile = vacantList[numVacantTiles - 1];
        vacantList[positionInVacantList] = lastVacantTile;
        vacantList[numVacantTiles - 1] = x;
        vacantMap[lastVacantTile] = positionInVacantList;
        vacantMap[x] = -1;
        --numVacantTiles;
    }

    private Position getRandomVacantPosition() {
        int tile = vacantList[random.nextInt(this.numVacantTiles)];
        int relX = tile % this.gameBoard.nCols;
        int relY = tile / this.gameBoard.nCols;
        return new Position(relX * TILE_SIZE, relY * TILE_SIZE);
    }

    public void createComponentsForGameBoard() {
        createSnake();
        createFood();
    }

    public void addComponentsToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.food);
        this.gameBoard.addDrawableGameComponent(this.snake);
    }

    public void attachEventListeners() {
        this.eventHandler.addListener(this.snake);
    }

    private void createFood() {
        this.food = new Food(this.sketch, FOOD_SIZE);
        this.food.setPosition(Position.getRandomPosition(this.gameBoard.nCols / 4, this.gameBoard.nRows / 4)
                .scale(TILE_SIZE, TILE_SIZE));
        this.food.setColor(220, 0, 0, 220);
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, SNAKE_BODY_PART_SIZE);
        this.snake.createBody();
        this.snake.setPosition(Position.getRandomPosition(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2)
                .scale(TILE_SIZE, TILE_SIZE));
        this.snake.setColor(0, 0, 220, 220);
    }

    public void updateGameBoard() {
        if (this.sketch.frameCount % SPEED == 0 && shouldKeepRunning()) {
            this.gameBoard.update();
            controlSnakeIfAteFood();
            adjustVacantTileCache();
        }
    }

    private boolean shouldKeepRunning() {
        if (this.numVacantTiles == 0 || hasSnakeHitEdge() || this.snake.ateItself()) {
            this.isGameRunning = false;
        }
        return this.isGameRunning;
    }

    private boolean hasSnakeHitEdge() {
        Position snakePosition = this.snake.getNextPosition();
        if (snakePosition.x < 0 || snakePosition.x >= this.sketch.width || snakePosition.y < 0
                || snakePosition.y >= this.sketch.height)
            return true;
        return false;
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

    private void controlSnakeIfAteFood() {
        Position snakePosition = this.snake.getPosition();
        Position foodPosition = this.food.getPosition();
        if (snakePosition.equals(foodPosition)) {
            this.snake.eatFood();
            this.food.setPosition(getRandomVacantPosition());
            fillPosition(this.food.getPosition());
        }
    }

    private void adjustVacantTileCache() {
        Position toFree = this.snake.prevTailPosition;
        Position toFill = this.snake.getPosition();
        vacatePosition(toFree);
        fillPosition(toFill);
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