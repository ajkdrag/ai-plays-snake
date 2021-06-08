package src.fsm.states;

import src.controller.GameController;
import src.evt.State;

public class RunningState implements GameState {

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case ' ':
                game.eventHandler.setEventState(State.KEY_PRESSED_SPACE);
                game.gameState = GameState.pausedState;
                break;
        }
    }

    @Override
    public void update(GameController game) {
        if (game.hasGameEnded()) {
            game.gameState = GameState.endedState;
            return;
        }
        game.updateGameBoard();
        game.updateTileStates();
    }

}
