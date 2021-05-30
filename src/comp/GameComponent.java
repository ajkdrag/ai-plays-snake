package src.comp;

import src.utils.Position;

public class GameComponent {
    protected Position position = new Position(0, 0);

    public void update() {
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Position getPosition() {
        return this.position;
    }
}