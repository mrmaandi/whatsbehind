package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamejam.textloader.Text;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class StartScreen implements Screen {

    private final WhatBehindTheDoorGame game;
    private final Stage stage;

    public StartScreen(final WhatBehindTheDoorGame game) {
        this.game = game;

        String logo = game.assetManager.get("logo.txt", Text.class).getString();

        // Create table
        Table rootTable = new Table();
        rootTable.debugAll();
        rootTable.setSkin(game.skin);
        rootTable.background("window");
        rootTable.setFillParent(true);
        rootTable.row();

        // Create UI elements
        Label logoLabel = new Label(logo, game.skin);
        rootTable.add(logoLabel);

        rootTable.row().height(200);
        Label hintLabel = new Label("Press any key to continue", game.skin);
        rootTable.add(hintLabel);

        stage = new Stage(new ScreenViewport());
        stage.addActor(rootTable);
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

        //game.font.draw(game.batch, logo, 0, 480);
        //game.font.draw(game.batch, "Press any key to start", 100, 100);
        //game.batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(game.gameScreen);
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
