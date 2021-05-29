package src;

import processing.core.PApplet;
import src.controller.GameController;

class App extends PApplet {
    static int W = 640;
    static int H = 360;
    GameController gameController;

    public static void main(String[] args) {
        String[] processingArgs = { "AI-Plays-SpaceInvaders" };
        App app = new App();
        app.setupGameController();
        PApplet.runSketch(processingArgs, app);
    }

    private void setupGameController() {
        this.gameController = new GameController(this);
        this.gameController.createGameBoard();
        this.gameController.createComponentsForGameBoard();
        this.gameController.addComponentsToGameBoard();
    }

    public void settings() {
        size(W, H);
    }

    public void draw() {
        background(220);
        this.gameController.renderGameBoard();
    }
}