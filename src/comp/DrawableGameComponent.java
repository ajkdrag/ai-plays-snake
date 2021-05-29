package src.comp;

import processing.core.PApplet;
import src.utils.Color;

public abstract class DrawableGameComponent extends GameComponent implements Drawable {
    public Color color;
    public PApplet sketch;

    public DrawableGameComponent(PApplet sketch) {
        this.sketch = sketch;
    }

    public void render() {
    }
}