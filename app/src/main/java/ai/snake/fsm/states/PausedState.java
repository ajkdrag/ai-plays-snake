package ai.snake.fsm.states;

import ai.snake.controller.GameController;
import ai.snake.evt.State;

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