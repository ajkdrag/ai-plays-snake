package ai.snake.utils;

import java.util.Random;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position getRandomPosition(int x_max, int y_max) {
        Random random = new Random();
        int rand_x = random.nextInt(x_max);
        int rand_y = random.nextInt(y_max);
        return new Position(rand_x, rand_y);
    }

    public Position scale(int factorX, int factorY) {
        this.x *= factorX;
        this.y *= factorY;
        return this;
    }

    // getters

    public int getRelation(int directionFirst, Position second) {
        // -1 : second point lies in the same direction/line
        // 0 : second point lies to the left
        // 1 : second point lies to the right
        int x2 = second.x;
        int y2 = second.y;
        int ans = -1;
        switch (directionFirst) {
            // down
            case 2:
                ans = x2 < this.x ? 1 : (x2 > this.x ? 0 : -1);
                break;
            // up
            case 0:
                ans = x2 < this.x ? 0 : (x2 > this.x ? 1 : -1);
                break;
            // left
            case 3:
                ans = y2 > this.y ? 0 : (y2 < this.y ? 1 : -1);
                break;
            // right
            case 1:
                ans = y2 > this.y ? 1 : (y2 < this.y ? 0 : -1);
                break;
            default:
                break;
        }
        return ans;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Position otherPosition = (Position) other;
        return this.x == otherPosition.x && this.y == otherPosition.y;
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
