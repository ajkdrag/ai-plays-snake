package ai.snake.comp;

import ai.snake.utils.Position;

public abstract class GameComponent {
    protected Position position = new Position(0, 0);

    public abstract void update();

    // setters

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    // getters
    
    public Position getPosition() {
        return this.position;
    }
}