package src.utils;

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

    public int getRelation(int directionFirst, Position second) {
        int x2 = second.x;
        int y2 = second.y;
        int ans = 0;
        switch (directionFirst) {
            case 0:
                ans = x2 < this.x ? 0 : 1;
                break;
            case 2:
                ans = x2 <= this.x ? 1 : 0;
                break;
            case 1:
                ans = y2 > this.y ? 0 : 1;
                break;
            case 3:
                ans = y2 >= this.y ? 1 : 0;
                break;
            default:
                break;
        }
        return ans;
    }

    public int willHitBoundary(int directionFirst, int boundDown, int boundUp, int boundLeft, int boundRight) {
        int ans = 0;
        switch (directionFirst) {
            case 0:
                ans = this.y <= boundUp ? 1 : 0;
                break;
            case 1:
                ans = this.x >= boundRight ? 1 : 0;
                break;
            case 2:
                ans = this.y >= boundDown ? 1 : 0;
                break;
            case 3:
                ans = this.x <= boundLeft ? 1 : 0;
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
