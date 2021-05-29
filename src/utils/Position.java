package src.utils;

import java.util.Random;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    static Position getRandomPosition(int x_max, int y_max) {
        Random random = new Random();
        int rand_x = random.nextInt(x_max);
        int rand_y = random.nextInt(y_max);
        return new Position(rand_x, rand_y);
    }
}
