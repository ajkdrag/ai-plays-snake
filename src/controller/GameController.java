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
    private PApplet sketch;

    private GameBoard gameBoard;
    private Food food;
    private Snake snake;

    private int numVacantTiles;
    private int[] vacantList, vacantMap;

    private EventHandler eventHandler;

    // lower = faster
    private static final int SPEED = 10;
    private static final int TILE_SIZE = 40;

    private static final int SNAKE_LENGTH = 4;
    private static final int SNAKE_BODY_PART_SIZE = TILE_SIZE;

    private static final int FOOD_SIZE = TILE_SIZE;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
        this.eventHandler = new EventHandler();
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
        vacantMap[lastVacantTile] = positionInVacantList;
        vacantList[numVacantTiles - 1] = x;
        vacantMap[x] = -1;
        --numVacantTiles;
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

    private Position getNewFoodPosition() {
        return Position.getRandomPosition(this.gameBoard.nCols / 4, this.gameBoard.nRows / 4).scale(TILE_SIZE,
                TILE_SIZE);
    }

    private void createSnake() {
        this.snake = new Snake(this.sketch, SNAKE_LENGTH, SNAKE_BODY_PART_SIZE);
        this.snake.createBody();
        this.snake.setPosition(
                new Position(this.gameBoard.nCols / 2, this.gameBoard.nRows / 2).scale(TILE_SIZE, TILE_SIZE));
        this.snake.setColor(0, 0, 220, 220);
    }

    public void updateGameBoard() {
        if (this.sketch.frameCount % SPEED == 0) {
            this.gameBoard.update();
            controlSnakeIfCrossedEdge();
            controlSnakeIfAteFood();
            adjustVacantTileCache();
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

    private void controlSnakeIfAteFood() {
        Position snakePosition = this.snake.getPosition();
        Position foodPosition = this.food.getPosition();
        if (snakePosition.equals(foodPosition)) {
            this.snake.eatFood();
            this.food.setPosition(getNewFoodPosition());
        }
    }

    private void adjustVacantTileCache() {
        Position toFree = this.snake.prevTailPosition;
        Position toFill = this.snake.getPosition();
        fillPosition(toFill);
        vacatePosition(toFree);
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