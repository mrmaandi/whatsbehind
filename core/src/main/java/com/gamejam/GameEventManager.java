package com.gamejam;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class GameEventManager {

    private final List<ChangeListener> listeners = new ArrayList<>();


    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    void receiveStateUpdate() {
        ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
        for (ChangeListener listener : listeners) {
            listener.handle(event);
        }
    }

}
