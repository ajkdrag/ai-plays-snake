package ai.snake.evt;

import java.util.HashSet;

public class EventHandler {
    private HashSet<EventListener> listeners;
    private Event event;

    public EventHandler() {
        this.listeners = new HashSet<>();
        this.event = new Event();
    }

    public void reset() {
        this.listeners.clear();
    }

    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        if (this.listeners.contains(listener))
            this.listeners.remove(listener);
    }

    public void handleEvent() {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    // setters

    public void setEventState(State state) {
        this.event.setState(state);
    }
}