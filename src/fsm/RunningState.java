package src.fsm;

import src.controller.GameController;
import src.evt.State;

public class RunningState implements GameState {

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case 'W':
                game.eventHandler.setEventState(State.KEY_PRESSED_UP);
                game.eventHandler.handleEvent();
                break;
            case 'S':
                game.eventHandler.setEventState(State.KEY_PRESSED_DOWN);
                game.eventHandler.handleEvent();
                break;
            case 'A':
                game.eventHandler.setEventState(State.KEY_PRESSED_LEFT);
                game.eventHandler.handleEvent();
                break;
            case 'D':
                game.eventHandler.setEventState(State.KEY_PRESSED_RIGHT);
                game.eventHandler.handleEvent();
                break;
            case '\t':
                game.eventHandler.setEventState(State.KEY_PRESSED_TAB);
                game.eventHandler.handleEvent();
                break;
            case 'R':
                game.eventHandler.setEventState(State.KEY_PRESSED_SHIFT);
                game.resetGame();
                break;
            case ' ':
                game.eventHandler.setEventState(State.KEY_PRESSED_SPACE);
                game.gameState = GameState.pausedState;
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
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
