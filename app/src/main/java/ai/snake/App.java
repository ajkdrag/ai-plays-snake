package ai.snake;

import processing.core.PApplet;
import ai.snake.controller.GameController;

class App extends PApplet {
    static int W = 640;
    static int H = 320;
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
        this.gameController = new GameController(this);
        this.gameController.resetGame();
        // while (true) {
        //     this.gameController.updateGame();
        // }
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
