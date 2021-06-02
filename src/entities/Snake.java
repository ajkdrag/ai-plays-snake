package src.entities;

import java.util.ArrayDeque;
import java.util.Iterator;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.evt.Event;
import src.evt.EventListener;
import src.utils.Position;

public class Snake extends DrawableGameComponent implements EventListener {
    class BodyPart extends DrawableGameComponent {
        private int side;

        public BodyPart(PApplet sketch, int side) {
            super(sketch);
            this.side = side;
        }

        public void render() {
            this.sketch.noFill();
            this.sketch.stroke(this.color.r, this.color.g, this.color.b, this.color.a);
            this.sketch.square(this.position.x, this.position.y, this.side);
        }

        @Override
        public void update() {
        }
    }

    private int length;
    private int direction = 3;
    private int[] dirX = { 0, 0, -1, 1 };
    private int[] dirY = { -1, 1, 0, 0 };
    private int bodyPartSize;
    private boolean isReadyToHandleEvent = true;
    private boolean hasEatenFood = false;
    private Position prevTailPosition;
    public ArrayDeque<BodyPart> body;

    public Snake(PApplet sketch, int length, int bodyPartSize) {
        super(sketch);
        this.length = length;
        this.bodyPartSize = bodyPartSize;
        this.body = new ArrayDeque<>(length);
        this.prevTailPosition = new Position(0, 0);
    }

    public void createBody() {
        for (int i = 0; i < length; ++i) {
            this.body.add(new BodyPart(this.sketch, this.bodyPartSize));
        }
    }

    private void updatePrevTailPosition(Position tailPosition) {
        this.prevTailPosition.x = tailPosition.x;
        this.prevTailPosition.y = tailPosition.y;
    }

    public void eatFood() {
        this.hasEatenFood = true;
        this.length++;
    }

    public boolean hasEatenItself() {
        Iterator<BodyPart> it = this.body.descendingIterator();
        // ignore the head itself; check with body
        it.next();
        while (it.hasNext()) {
            BodyPart bodyPart = it.next();
            if (bodyPart.getPosition().equals(this.position))
                return true;
        }
        return false;
    }

    public boolean hasSnakeHitEdge() {
        Position snakePosition = getNextPosition();
        if (snakePosition.x < 0 || snakePosition.x + this.bodyPartSize > this.sketch.width || snakePosition.y < 0
                || snakePosition.y + this.bodyPartSize > this.sketch.height)
            return true;
        return false;
    }

    public boolean isDirectionValid() {
        return this.direction >= 0 && this.direction < 4;
    }

    @Override
    public void update() {
        // sliding window concept for snake movement
        if (isDirectionValid()) {
            Position tailPosition = this.body.peekFirst().getPosition();
            BodyPart newHead = new BodyPart(this.sketch, this.bodyPartSize);
            newHead.setColor(this.color);
            this.body.offerLast(newHead);
            if (!this.hasEatenFood) {
                // remove tail
                this.body.pollFirst();
                updatePrevTailPosition(tailPosition);
            }
            setHeadPosition(getNextPosition());
            this.hasEatenFood = false;
        }
        this.isReadyToHandleEvent = true;
    }

    @Override
    public void render() {
        for (BodyPart bodyPart : this.body) {
            bodyPart.render();
        }
    }

    @Override
    public void onEvent(Event event) {
        if (!this.isReadyToHandleEvent)
            return;
        switch (event.getState()) {
            case KEY_PRESSED_UP:
                if (this.direction != 1)
                    setDirection(0);
                break;
            case KEY_PRESSED_DOWN:
                if (this.direction != 0)
                    setDirection(1);
                break;
            case KEY_PRESSED_LEFT:
                if (this.direction != 3)
                    setDirection(2);
                break;
            case KEY_PRESSED_RIGHT:
                if (this.direction != 2)
                    setDirection(3);
                break;
            default:
                break;
        }
    }

    // setters

    public void setHeadPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
        this.body.peekLast().setPosition(x, y);
    }

    public void setHeadPosition(Position position) {
        setHeadPosition(position.x, position.y);
    }

    private void setDirection(int newDirection) {
        this.direction = newDirection;
        this.isReadyToHandleEvent = false;
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

    @Override
    public void setColor(int r, int g, int b, int a) {
        this.color.r = r;
        this.color.g = g;
        this.color.b = b;
        this.color.a = a;
        for (BodyPart bodyPart : this.body) {
            bodyPart.setColor(this.color);
        }
    }

    // getters

    public Position getNextPosition() {
        return new Position(this.position.x + dirX[direction] * this.bodyPartSize,
                this.position.y + dirY[direction] * this.bodyPartSize);
    }

    public Position getPrevTailPosition() {
        return this.prevTailPosition;
    }
}
