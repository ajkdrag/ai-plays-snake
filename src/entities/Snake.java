package src.entities;

import java.util.ArrayDeque;
import java.util.Iterator;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.evt.Event;
import src.evt.EventListener;
import src.utils.Position;

public class Snake extends DrawableGameComponent implements EventListener {
    int length;
    int direction = 3;
    int steps = 1;
    int[] dirX = { 0, 0, -1, 1 };
    int[] dirY = { -1, 1, 0, 0 };
    int bodyPartSize;
    ArrayDeque<BodyPart> body;
    boolean isEventInQueue = false;

    public Snake(PApplet sketch, int length, int bodyPartSize) {
        super(sketch);
        this.length = length;
        this.bodyPartSize = bodyPartSize;
        this.body = new ArrayDeque<>(length);
    }

    public void createBody() {
        for (int i = 0; i < length; ++i) {
            this.body.add(new BodyPart(this.sketch, this.bodyPartSize));
        }
    }

    @Override
    public void setPosition(Position headPosition) {
        this.position = headPosition;
        Iterator<BodyPart> it = this.body.descendingIterator();
        int currX = headPosition.x;
        int currY = headPosition.y;
        while (it.hasNext()) {
            BodyPart bodyPart = it.next();
            bodyPart.setPosition(currX, currY);
            currX -= this.bodyPartSize;
        }
    }

    public void setHeadPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
        this.body.peekLast().setPosition(x, y);
    }

    @Override
    public void setColor(int r, int g, int b, int a) {
        for (BodyPart bodyPart : this.body) {
            bodyPart.setColor(r, g, b, a);
        }
    }

    @Override
    public void update() {
        if (direction >= 0 && direction < 4) {
            for (int i = 0; i < steps; ++i) {
                BodyPart tail = body.pollFirst();
                body.offerLast(tail);
                setHeadPosition(this.position.x + dirX[direction] * this.bodyPartSize,
                        this.position.y + dirY[direction] * this.bodyPartSize);
            }
        }
        this.isEventInQueue = false;
    }

    @Override
    public void render() {
        for (BodyPart bodyPart : this.body) {
            bodyPart.render();
        }
    }

    @Override
    public void onEvent(Event event) {
        if (this.isEventInQueue)
            return;
        this.isEventInQueue = true;
        switch (event.state) {
            case KEY_PRESSED_UP:
                if (this.direction != 1)
                    this.direction = 0;
                break;
            case KEY_PRESSED_DOWN:
                if (this.direction != 0)
                    this.direction = 1;
                break;
            case KEY_PRESSED_LEFT:
                if (this.direction != 3)
                    this.direction = 2;
                break;
            case KEY_PRESSED_RIGHT:
                if (this.direction != 2)
                    this.direction = 3;
                break;
            default:
                break;
        }
    }

    class BodyPart extends DrawableGameComponent {
        private int side;

        public BodyPart(PApplet sketch, int side) {
            super(sketch);
            this.side = side;
        }

        public void render() {
            this.sketch.stroke(this.color.r, this.color.g, this.color.b, this.color.a);
            this.sketch.square(this.position.x, this.position.y, this.side);
        }

    }
}
