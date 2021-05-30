package src.entities;

import java.util.ArrayDeque;
import java.util.Iterator;

import processing.core.PApplet;
import src.comp.DrawableGameComponent;
import src.utils.Position;

public class Snake extends DrawableGameComponent {
    int length;
    int bodyPartSize;
    ArrayDeque<BodyPart> body;

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
            currX -= bodyPartSize;
        }
    }

    @Override
    public void setColor(int r, int g, int b, int a) {
        for (BodyPart bodyPart : this.body) {
            bodyPart.setColor(r, g, b, a);
        }
    }

    public void render() {
        for (BodyPart bodyPart : this.body) {
            bodyPart.render();
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
