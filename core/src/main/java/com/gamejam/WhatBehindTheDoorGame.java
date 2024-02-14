package com.gamejam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamejam.textloader.Text;
import com.gamejam.textloader.TextLoader;

import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class WhatBehindTheDoorGame extends Game {

    GameState gameState;
    AssetManager assetManager;
    Skin skin;

    @Override
    public void create() {
        gameState = new GameState();
        gameState.level = 1;
        gameState.playerHealth = 3;
        gameState.coins = 0;
        Random random = new Random();
        gameState.winningOption = random.nextInt(3);

        assetManager = new AssetManager();
        assetManager.setLoader(
            Text.class,
            new TextLoader(
                new InternalFileHandleResolver()
            )
        );
        assetManager.load(new AssetDescriptor<>("logo.txt", Text.class, new TextLoader.TextParameter()));
        assetManager.load("commodore64/raw/commodore-64.fnt", BitmapFont.class);
        assetManager.load("commodore64/skin/uiskin.json", Skin.class, new SkinLoader.SkinParameter("commodore64/skin/uiskin.atlas"));
        assetManager.finishLoading();

        skin = assetManager.get("commodore64/skin/uiskin.json", Skin.class);

        setScreen(new StartScreen(this));
    }

    public void dispose() {
        skin.dispose();
    }
}
