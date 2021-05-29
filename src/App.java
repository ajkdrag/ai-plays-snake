package src;

import processing.core.PApplet;

class App extends PApplet {
    static int W = 300;
    static int H = 400;

    public static void main(String[] args) {
        String[] processingArgs = { "AI-Plays-SpaceInvaders" };
        App app = new App();
        PApplet.runSketch(processingArgs, app);
    }

    public void settings() {
        size(W, H);
    }

    public void draw() {
        background(255);
    }
}