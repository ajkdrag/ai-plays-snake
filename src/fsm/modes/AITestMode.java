package src.fsm.modes;

import src.controller.GameController;
import src.evt.State;

public class AITestMode implements GameMode {
    @Override
    public void enter() {

    }

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case '\t':
                game.eventHandler.setEventState(State.KEY_PRESSED_TAB);
                game.eventHandler.handleEvent();
                break;
            case 'R':
                game.resetGame();
                game.gameMode = GameMode.manualMode;
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        game.gameState.handleInput(game, keyCode);
    }

    @Override
    public void update(GameController game) {
        // TODO Auto-generated method stub

    }

}
