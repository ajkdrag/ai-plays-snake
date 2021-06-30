package src.fsm.modes;

import src.controller.GameController;

public interface GameMode {
    public static ManualMode manualMode = new ManualMode();
    public static AITestMode aiTestMode = new AITestMode();
    public static AITrainMode aiTrainMode = new AITrainMode();

    public void enter(GameController game);

    public void handleInput(GameController game, int keyCode);

    public void update(GameController game);
}
