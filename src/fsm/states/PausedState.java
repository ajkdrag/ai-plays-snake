package src.fsm.states;

import src.controller.GameController;
import src.evt.State;

public class PausedState implements GameState {

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case ' ':
                game.eventHandler.setEventState(State.KEY_PRESSED_SPACE);
                game.gameState = GameState.runningState;
                break;
        }
    }

    @Override
    public void update(GameController game) {
    }

}