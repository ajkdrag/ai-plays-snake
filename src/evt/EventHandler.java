package src.evt;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    private List<EventListener> listeners;
    private Event event;

    public EventHandler() {
        this.listeners = new ArrayList<>();
        this.event = new Event();
    }

    public void addListener(EventListener listener) {
        this.listeners.add(listener);
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