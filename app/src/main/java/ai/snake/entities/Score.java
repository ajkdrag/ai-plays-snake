package ai.snake.entities;

import processing.core.PApplet;
import ai.snake.comp.DrawableGameComponent;
import ai.snake.evt.Event;
import ai.snake.evt.EventListener;
import ai.snake.evt.State;

public class Score extends DrawableGameComponent implements EventListener {
    private int score;
    private int textSize;
    private String text;

    public Score(PApplet sketch, int textSize) {
        super(sketch);
        this.text = "Score : %d";
        this.textSize = textSize;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getState() == State.FOOD_EATEN) {
            ++this.score;
        }
    }

    @Override
    public void render() {
        this.sketch.textSize(this.textSize);
        this.sketch.fill(this.color.r, this.color.g, this.color.b, this.color.a);
        this.sketch.text(String.format(this.text, this.score), this.position.x, this.position.y);
    }

    @Override
    public void update() {
    }

    // getters
    public int getScore() {
        return this.score;
    }
}
