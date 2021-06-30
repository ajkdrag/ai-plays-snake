package src;

import processing.core.PApplet;
import src.controller.GameController;

class App extends PApplet {
    static int W = 640;
    static int H = 320;
    static int fRate = 200;
    GameController gameController;

    public static void main(String[] args) {
        String[] processingArgs = { "AI-Plays-Snake" };
        App app = new App();
        PApplet.runSketch(processingArgs, app);
    }

    public void settings() {
        setSize(W, H);
    }

    public void setup() {
        frameRate(fRate);
        this.gameController = new GameController(this);
        this.gameController.resetGame();
    }

    public void draw() {
        background(220);
        this.gameController.updateGame();
        this.gameController.renderGame();
    }

    public void keyPressed() {
        this.gameController.keyPressed();
    }
}