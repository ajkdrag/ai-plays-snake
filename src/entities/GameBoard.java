package src.entities;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;

public class GameBoard extends DrawableGameComponent {
    private List<DrawableGameComponent> components;
    private int tileSize;
    private int width, height;
    public int nRows, nCols;

    public GameBoard(PApplet sketch, int width, int height, int tileSize) {
        super(sketch);
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.nRows = height / tileSize;
        this.nCols = width / tileSize;
        this.components = new ArrayList<>();
    }

    public void addDrawableGameComponent(DrawableGameComponent component) {
        this.components.add(component);
    }

    public void reset() {
        this.components.clear();
    }

    public void update() {
        for (DrawableGameComponent component : components) {
            component.update();
        }
    }

    public void render() {
        displayBoard();
        for (DrawableGameComponent component : components) {
            component.render();
        }
    }

    private void displayBoard() {
        this.sketch.noFill();
        this.sketch.stroke(this.color.r, this.color.g, this.color.b, this.color.a);

        for (int i = 0; i < nRows; ++i) {
            this.sketch.line(0, i * tileSize, width, i * tileSize);
        }
        for (int i = 0; i < nCols; ++i) {
            this.sketch.line(i * tileSize, 0, i * tileSize, height);
        }
    }
}
