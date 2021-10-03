package ai.snake.utils;

public class Color {
    public int r, g, b, a;

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public String toString() {
        return this.r + " " + this.g + " " + this.b + " " + this.a;
    }
}
