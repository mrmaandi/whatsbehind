package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamejam.textloader.Text;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

/** Screen for the main menu. */
public class MainMenuScreen implements Screen {

  private final WhatBehindTheDoorGame game;

  private final Stage stage;

  Table rootTable;

  public MainMenuScreen(final WhatBehindTheDoorGame game) {
    this.game = game;

    // Create table
    rootTable = new Table();
    // rootTable.debugAll();
    rootTable.row().height(40).width(300);
    rootTable.setSkin(game.skin);
    rootTable.background("window");
    rootTable.setFillParent(true);

    setContent();

    // Create stage
    stage = new Stage(new FitViewport(1280, 720));
    stage.addActor(rootTable);
  }

  private void setContent() {
    // Create UI elements
    rootTable.row();
    String logo = game.assetManager.get("logo.txt", Text.class).getString();
    Label logoLabel = new Label(logo, game.skin);
    rootTable.add(logoLabel);

    rootTable.row().padTop(200).height(40).width(300);
    TextButton buttonGame = new TextButton("To game", game.skin);
    buttonGame.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(game.gameScreen);
          }
        });
    rootTable.add(buttonGame);

    rootTable.row().padTop(20).height(40).width(300);
    TextButton buttonCredits = new TextButton("Credits", game.skin);
    buttonCredits.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(game.creditsScreen);
          }
        });
    rootTable.add(buttonCredits);
  }

  @Override
  public void show() {
    // Prepare your screen here.
    Gdx.input.setInputProcessor(stage);
    stage.getRoot().getColor().a = 0;
    stage.getRoot().addAction(fadeIn(0.5f));
  }

  @Override
  public void render(float delta) {
    // Draw your screen here. "delta" is the time since last render in seconds.
    // ScreenUtils.clear(0, 0, 0, 1);

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
