package src.fsm.modes;

import src.controller.GameController;
import src.evt.State;

public class ManualMode implements GameMode {
    @Override
    public void enter() {

    }

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
                game.resetGame();
                break;
            case 'Q':
                game.gameMode = GameMode.aiTestMode;
                game.gameMode.enter();
                break;
            case 'T':
                game.resetGame();
                game.gameMode = GameMode.aiTrainMode;
                game.gameMode.enter();
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }

        game.gameState.handleInput(game, keyCode);
    }

    @Override
    public void update(GameController game) {
        game.gameState.update(game);
    }

}
