package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamejam.textloader.Text;

/** Screen for the credits. */
public class CreditsScreen implements Screen {

  private final WhatBehindTheDoorGame game;

  private final Stage stage;
  private final String credits;
  Table rootTable;

  public CreditsScreen(final WhatBehindTheDoorGame game) {
    this.game = game;

    // Create table
    rootTable = new Table();
    // rootTable.debugAll();
    rootTable.row().height(40).width(300);
    rootTable.setSkin(game.skin);
    rootTable.background("window");
    rootTable.setFillParent(true);

    credits = game.assetManager.get("credits.txt", Text.class).getString();

    setContent();

    // Create stage
    stage = new Stage(new ScreenViewport());
    stage.addActor(rootTable);
  }

  private void setContent() {
    // Create UI elements
    rootTable.row().expand().fill();
    Label label = new Label(credits, game.skin);
    label.setWrap(true);
    label.setAlignment(Align.center);
    rootTable.add(label);

    rootTable.row().padTop(20);

    TextButton buttonCredits = new TextButton("Back", game.skin);
    buttonCredits.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(game.mainMenuScreen);
          }
        });
    rootTable.add(buttonCredits);
  }

  @Override
  public void show() {
    // Prepare your screen here.
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    // Draw your screen here. "delta" is the time since last render in seconds.
    ScreenUtils.clear(0, 0, 0, 1);

    stage.act(delta);
    stage.draw();
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
