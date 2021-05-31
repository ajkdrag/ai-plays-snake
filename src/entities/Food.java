package src.entities;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;

public class Food extends DrawableGameComponent {
    private int side;

    public Food(PApplet sketch, int side) {
        super(sketch);
        this.side = side;
    }

    @Override
    public void render() {
        this.sketch.noFill();
        this.sketch.stroke(this.color.r, this.color.g, this.color.b, this.color.a);
        this.sketch.square(this.position.x, this.position.y, this.side);
    }

    @Override
    public void update() {
    }
}
