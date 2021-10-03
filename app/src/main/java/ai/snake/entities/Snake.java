package ai.snake.entities;

import java.util.ArrayDeque;
import java.util.Iterator;

import processing.core.PApplet;
import ai.snake.comp.DrawableGameComponent;
import ai.snake.evt.Event;
import ai.snake.evt.EventListener;
import ai.snake.utils.Position;

public class Snake extends DrawableGameComponent implements EventListener {
    class BodyPart extends DrawableGameComponent {
        private int side;

        public BodyPart(PApplet sketch, int side) {
            super(sketch);
            this.side = side;
        }

        public void render() {
            this.sketch.noStroke();
            this.sketch.fill(this.color.r, this.color.g, this.color.b, this.color.a);
            this.sketch.rect(this.position.x, this.position.y, this.side, this.side);
        }

        @Override
        public void update() {
        }
    }

    private int length;
    private int assumedDirection = 1;
    private int traversedDirection = 1;
    public int prevDirection = 0;
    private int[] dirX = { 0, 1, 0, -1 };
    private int[] dirY = { -1, 0, 1, 0 };
    private int bodyPartSize;
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

    public int hasWallInThisDirection(int direction) {
        int ans = 0;
        switch (direction) {
            case 0:
                ans = this.position.y <= 0 ? 1 : 0;
                break;
            case 1:
                ans = this.position.x + this.bodyPartSize >= this.sketch.width ? 1 : 0;
                break;
            case 2:
                ans = this.position.y + this.bodyPartSize >= this.sketch.height ? 1 : 0;
                break;
            case 3:
                ans = this.position.x <= 0 ? 1 : 0;
                break;
            default:
                break;
        }
        return ans;
    }

    public int hasBodyPartInThisDirection(int direction) {
        int ans = 0;
        Position nextPos = getNextPosition(direction);
        for (BodyPart part : this.body) {
            if (part.getPosition().equals(nextPos)) {
                ans = 1;
                break;
            }
        }
        return ans;
    }

    public boolean isDirectionValid() {
        return this.assumedDirection >= 0 && this.assumedDirection < 4;
    }

    @Override
    public void update() {
        // sliding window concept for snake movement
        if (isDirectionValid()) {
            if (assumedDirection != this.traversedDirection)
                prevDirection = this.traversedDirection;
            this.traversedDirection = assumedDirection;
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
    }

    @Override
    public void render() {
        for (BodyPart bodyPart : this.body) {
            bodyPart.render();
        }
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getState()) {
            case KEY_PRESSED_UP:
                if (this.traversedDirection != 2)
                    setDirection(0);
                break;
            case KEY_PRESSED_DOWN:
                if (this.traversedDirection != 0)
                    setDirection(2);
                break;
            case KEY_PRESSED_LEFT:
                if (this.traversedDirection != 1)
                    setDirection(3);
                break;
            case KEY_PRESSED_RIGHT:
                if (this.traversedDirection != 3)
                    setDirection(1);
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
        this.assumedDirection = newDirection;
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
        return new Position(this.position.x + dirX[this.assumedDirection] * this.bodyPartSize,
                this.position.y + dirY[this.assumedDirection] * this.bodyPartSize);
    }

    public Position getNextPosition(int direction) {
        int newX = this.position.x;
        int newY = this.position.y;
        switch (direction) {
            case 0:
                newY -= this.bodyPartSize;
                break;
            case 1:
                newX += this.bodyPartSize;
                break;
            case 2:
                newY += this.bodyPartSize;
                break;
            case 3:
                newX -= this.bodyPartSize;
                break;
            default:
                break;
        }
        return new Position(newX, newY);
    }

    public Position getTailPosition() {
        return this.body.getFirst().getPosition();
    }

    public Position getPrevTailPosition() {
        return this.prevTailPosition;
    }

    public int getDirection() {
        return this.traversedDirection;
    }
}
