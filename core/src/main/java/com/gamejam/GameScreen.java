package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Main screen for the game.
 */
public class GameScreen implements Screen {

    public static final String DOOR_ASCII =
        "________\n" +
            "  __  __  \n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |__||__| \n" +
            "  __  __()\n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |__||__| \n" +
            "__________";
    private final WhatBehindTheDoorGame game;
    private final Stage stage;

    Label label2;
    Table rootTable;


    public GameScreen(final WhatBehindTheDoorGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        // Prepare your screen here.
        // Create table
        rootTable = new Table();

        rootTable.debugAll();
        rootTable.setSkin(game.skin);
        rootTable.background("window");
        rootTable.setFillParent(true);

        // Create UI elements

        // Top section
        rootTable.row().expand().fill();
        Window window = new Window("", game.skin, "dialog");
        window.setMovable(false);
        // window.debugAll();
        window.row();
        Label label = new Label("Pick a door", game.skin);
        window.add(label);
        window.row();
        Button option1 = new TextButton(DOOR_ASCII, game.skin);
        option1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameState.level += 1;
                label2.setText("Level: " + game.gameState.level);
            }
        });
        Button option2 = new TextButton(DOOR_ASCII, game.skin);
        ClickListener clickListener = option2.getClickListener();
        clickListener.clicked(new InputEvent(), 0, 0);
        Button button3 = new TextButton(DOOR_ASCII, game.skin);
        HorizontalGroup group = new HorizontalGroup();
        group.addActor(option1);
        group.addActor(option2);
        group.addActor(button3);
        window.add(group);

        rootTable.add(window);

        // Bottom section
        rootTable.row().expandX().fillX();
        label2 = new Label("Level: " + game.gameState.level, game.skin);
        rootTable.add(label2).expandX().fillX();

        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        stage.dispose();
    }
}
