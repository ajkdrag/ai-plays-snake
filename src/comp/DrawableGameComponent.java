package src.comp;

import processing.core.PApplet;
import src.utils.Color;

public abstract class DrawableGameComponent extends GameComponent implements Drawable {
    protected Color color = new Color(0, 0, 0, 0);
    public PApplet sketch;

    public DrawableGameComponent(PApplet sketch) {
        this.sketch = sketch;
    }

    public abstract void render();

    // setters
    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int r, int g, int b, int a) {
        this.color.r = r;
        this.color.g = g;
        this.color.b = b;
        this.color.a = a;
    }

    // getters

    public Color getColor() {
        return this.color;
    }
}