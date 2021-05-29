package src.comp;

import src.utils.Position;

public class BoundingBox {
    Position position;
    int width, height;

    public BoundingBox(int x, int y, int width, int height) {
        this(new Position(x, y), width, height);
    }

    public BoundingBox(Position position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

}
