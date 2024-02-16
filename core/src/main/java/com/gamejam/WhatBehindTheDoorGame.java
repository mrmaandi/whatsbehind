package com.gamejam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamejam.textloader.Text;
import com.gamejam.textloader.TextLoader;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class WhatBehindTheDoorGame extends Game {

  GameState gameState;
  AssetManager assetManager;
  GameEventManager eventManager;
  Skin skin;
  Screen startScreen;
  Screen mainMenuScreen;
  Screen gameScreen;
  Screen creditsScreen;

  @Override
  public void create() {
    gameState = new GameState();
    eventManager = new GameEventManager();
    gameState.level = 1;
    gameState.maxPlayerHealth = 3;
    gameState.playerHealth = gameState.maxPlayerHealth;
    gameState.playerChoiceConfirmed = false;
    // gameState.playerChoiceSecondConfirmed = false;
    gameState.coins = 0;
    gameState.readyForShop = false;
    gameState.winningOption = gameState.generateWinningOption();
    gameState.showHostNextButton = true;
    gameState.revealedDoorAnimationPlayed = false;
    gameState.showShop = false;

    assetManager = new AssetManager();
    assetManager.setLoader(Text.class, new TextLoader(new InternalFileHandleResolver()));
    assetManager.load(
        new AssetDescriptor<>("logo.txt", Text.class, new TextLoader.TextParameter()));
    assetManager.load("commodore64/raw/commodore-64.fnt", BitmapFont.class);
    assetManager.load(
        "commodore64/skin/uiskin.json",
        Skin.class,
        new SkinLoader.SkinParameter("commodore64/skin/uiskin.atlas"));
    assetManager.load("credits.txt", Text.class, new TextLoader.TextParameter());
    assetManager.finishLoading();

    skin = assetManager.get("commodore64/skin/uiskin.json", Skin.class);

    startScreen = new StartScreen(this);
    mainMenuScreen = new MainMenuScreen(this);
    gameScreen = new GameScreen(this);
    creditsScreen = new CreditsScreen(this);

    setScreen(startScreen);
  }

  public void dispose() {
    skin.dispose();
  }
}
