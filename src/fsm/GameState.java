package src.fsm;

import src.controller.GameController;

public interface GameState {
    public static PausedState pausedState = new PausedState();
    public static RunningState runningState = new RunningState();
    public static EndedState endedState = new EndedState();

    public void handleInput(GameController game, int keyCode);

    public void update(GameController game);
}
