package src.entities;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;

public class Snake extends DrawableGameComponent {
    int size;

    public Snake(PApplet sketch, int size) {
        super(sketch);
        this.size = 0;
    }

    public void render() {

    }
}
