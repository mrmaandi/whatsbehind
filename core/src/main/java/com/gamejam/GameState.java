package com.gamejam;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

public class GameState {

    Integer doorsAmount = null;
    Integer carPosition = null;
    Integer playerChoice = null;
    Integer winningOption = null;
    Integer level = null;
    Integer playerHealth = null;
    Integer coins = null;

    private List<ChangeListener> listeners;


    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    private void fireChangeEvent() {
        ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
        for (ChangeListener listener : listeners) {
            listener.handle(event);
        }
    }
}
