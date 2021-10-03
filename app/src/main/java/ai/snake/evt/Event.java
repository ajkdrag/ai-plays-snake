package ai.snake.evt;

public class Event {
    private State state = State.KEY_INVALID;

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }
}
