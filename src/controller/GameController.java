package src.controller;

import processing.core.PApplet;
import src.entities.Food;
import src.entities.GameBoard;
import src.utils.Color;

public class GameController {
    private GameBoard gameBoard;
    private Food food;

    private PApplet sketch;

    public GameController(PApplet sketch) {
        this.sketch = sketch;
    }

    public void resetGame() {
        this.gameBoard.reset();
        this.createComponentsForGameBoard();
        this.addComponentsToGameBoard();
    }

    public void createGameBoard() {
        this.gameBoard = new GameBoard(this.sketch);
    }

    public void createComponentsForGameBoard() {
        this.food = new Food(this.sketch, 50);
        this.food.color = new Color(220, 0, 0, 255);
    }

    public void addComponentsToGameBoard() {
        this.gameBoard.addDrawableGameComponent(this.food);
    }

    public void renderGameBoard() {
        this.gameBoard.render();
    }

}