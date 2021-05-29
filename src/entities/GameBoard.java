package src.entities;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;

public class GameBoard extends DrawableGameComponent {
    List<DrawableGameComponent> components;

    public GameBoard(PApplet sketch) {
        super(sketch);
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
        for (DrawableGameComponent component : components) {
            component.render();
        }
    }
}
