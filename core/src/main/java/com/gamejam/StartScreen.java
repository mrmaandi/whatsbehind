package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamejam.textloader.Text;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/** First screen of the application. Displayed after the application is created. */
public class StartScreen implements Screen {

  private final WhatBehindTheDoorGame game;
  private final Stage stage;

  public StartScreen(final WhatBehindTheDoorGame game) {
    this.game = game;

    String logo = game.assetManager.get("logo.txt", Text.class).getString();

    // Create table
    Table rootTable = new Table();
    // rootTable.debugAll();
    rootTable.setSkin(game.skin);
    rootTable.background("window");
    rootTable.setFillParent(true);
    rootTable.row();

    // Create UI elements
    Label logoLabel = new Label(logo, game.skin);
    rootTable.add(logoLabel);

    rootTable.row().padTop(200);
    Label hintLabel = new Label("Press any key or click the screen to continue", game.skin);
    hintLabel.addAction(
        forever(
            sequence(
                moveBy(0, 10, 1, Interpolation.smooth), moveBy(0, -10, 1, Interpolation.smooth))));
    rootTable.add(hintLabel);

    stage = new Stage(new FitViewport(1280, 720));
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
    // ScreenUtils.clear(0, 0, 0, 1);

    stage.act(delta);
    stage.draw();

    if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
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
