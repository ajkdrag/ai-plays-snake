package src.fsm;

import src.controller.GameController;
import src.evt.State;

public class PausedState implements GameState {

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case 'R':
                game.eventHandler.setEventState(State.KEY_PRESSED_SHIFT);
                game.resetGame();
                break;
            case ' ':
                game.eventHandler.setEventState(State.KEY_PRESSED_SPACE);
                game.gameState = GameState.runningState;
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
    }

    @Override
    public void update(GameController game) {
    }

}