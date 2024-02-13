package com.gamejam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamejam.textloader.Text;
import com.gamejam.textloader.TextLoader;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class WhatBehindTheDoorGame extends Game {

    //SpriteBatch batch;
    BitmapFont font;
    GameState gameState;
    AssetManager assetManager;
    Skin skin;

    @Override
    public void create() {
        gameState = new GameState();
        gameState.level = 1;
        //batch = new SpriteBatch();

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
        //font = assetManager.get( "commodore64/raw/commodore-64.fnt", BitmapFont.class);

        // font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // font.getData().setScale(1f);

        setScreen(new StartScreen(this));
    }

    public void dispose() {
        //batch.dispose();
        //font.dispose();
        skin.dispose();
        //assetManager.dispose();
    }
}
