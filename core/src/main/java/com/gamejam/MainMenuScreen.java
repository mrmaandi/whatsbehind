package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Screen for the main menu.
 */
public class MainMenuScreen implements Screen {

    private final WhatBehindTheDoorGame game;
    //private final OrthographicCamera camera;

    private final Stage stage;

    public MainMenuScreen(final WhatBehindTheDoorGame game) {
        this.game = game;

        // Create table
        Table rootTable = new Table();
        rootTable.debugAll();
        rootTable.setSkin(game.skin);
        rootTable.background("window");
        rootTable.setFillParent(true);
        rootTable.setLayoutEnabled(true);

        // Create UI elements
        Label nameLabel = new Label("Name:", game.skin);
        rootTable.add(nameLabel);

        Window window = new Window("WINDOW", game.skin, "dialog");
        window.debugAll();
        window.setResizable(true);
        window.getTitleLabel().setStyle(game.skin.get("title", Label.LabelStyle.class));

        Button button = new TextButton("Text", game.skin);
        window.add(button);

        // Create stage
        stage = new Stage(new ScreenViewport());
        stage.addActor(rootTable);
        stage.addActor(window);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        ScreenUtils.clear(0, 0, 0, 1);

        //camera.update();
        //game.batch.setProjectionMatrix(camera.combined);

        //game.batch.begin();
        //game.font.draw(game.batch, "What's Behind The Door?", 100, 150);
        //game.font.draw(game.batch, "Main menu", 100, 100);
        //game.batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.gameScreen);
            // dispose();
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
