package ai.snake.entities;

import processing.core.PApplet;
import ai.snake.comp.DrawableGameComponent;

public class Food extends DrawableGameComponent {
    private int side;

    public Food(PApplet sketch, int side) {
        super(sketch);
        this.side = side;
    }

    @Override
    public void render() {
        this.sketch.stroke(this.color.r, this.color.g, this.color.b, this.color.a);
        this.sketch.fill(this.color.r, this.color.g, this.color.b, this.color.a);
        this.sketch.rect(this.position.x, this.position.y, this.side, this.side);
    }

    @Override
    public void update() {
    }
}
