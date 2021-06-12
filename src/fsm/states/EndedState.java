package src.fsm.states;

import src.controller.GameController;
import src.evt.State;

public class EndedState implements GameState {

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case 'R':
                game.eventHandler.setEventState(State.KEY_PRESSED_SHIFT);
                game.resetGame();
                break;
        }
    }

    @Override
    public void update(GameController game) {
        
    }

}
