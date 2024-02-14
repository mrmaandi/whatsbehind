package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Main screen for the game.
 */
public class GameScreen implements Screen {

    public static final String DOOR_ASCII =
            "  __  __  \n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |__||__| \n" +
            "  __  __()\n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |  ||  | \n" +
            " |__||__| \n";
    private final WhatBehindTheDoorGame game;
    private final Stage stage;

    Label labelLevel;
    Table rootTable;


    public GameScreen(final WhatBehindTheDoorGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        // Prepare your screen here.
        // Create table
        rootTable = new Table(game.skin);

        rootTable.debugAll();
        rootTable.background("window");
        rootTable.setFillParent(true);

        // Create UI elements

        // Top section
        rootTable.row().expand().fill();
        // Left pane
        Table tableLeft = new Table();
        tableLeft.row().expandX().fillX().expandY().fillY();
        Window window = new Window("", game.skin, "dialog");
        //window.debugAll();
        window.setMovable(false);
        window.getTitleTable().add(new Label("Game", game.skin, "title")).expandX().left();
        window.row().height(40);
        Label label = new Label("Pick a door", game.skin);
        window.add(label);
        window.row();
        Button option1 = new TextButton(DOOR_ASCII, game.skin);
        String statsLabel = "Level: " + game.gameState.level + " | " + "Coins: " + game.gameState.coins + " | " + "Health: " + game.gameState.playerHealth;
        option1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameState.level += 1;
                String statsLabel = "Level: " + game.gameState.level + " | " + "Coins: " + game.gameState.coins + " | " + "Health: " + game.gameState.playerHealth;
                labelLevel.setText(statsLabel);
            }
        });
        Button option2 = new TextButton(DOOR_ASCII, game.skin);
        ClickListener clickListener = option2.getClickListener();
        clickListener.clicked(new InputEvent(), 0, 0);
        Button option3 = new TextButton(DOOR_ASCII, game.skin);
        HorizontalGroup group = new HorizontalGroup();
        group.space(10);
        group.addActor(option1);
        option1.addAction(sequence(moveBy(0, 50), parallel(fadeIn(2), moveBy(0, -50, 5, Interpolation.bounceOut))));
        group.addActor(option2);
        option2.addAction(sequence(moveBy(0, 50), delay(0.5f), parallel(fadeIn(2), moveBy(0, -50, 5, Interpolation.bounceOut))));
        group.addActor(option3);
        option3.addAction(sequence(moveBy(0, 50), delay(1), parallel(fadeIn(2), moveBy(0, -50, 5, Interpolation.bounceOut))));
        window.add(group);
        tableLeft.add(window);
        // Left pane - Bottom section
        tableLeft.row().expandX().fillX().bottom();

        labelLevel = new Label(statsLabel, game.skin);
        labelLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        tableLeft.add(labelLevel);

        // Right pane
        Table tableRight = new Table();
        Label labelRight = new Label("Shop", game.skin);
        tableRight.add(labelRight);

        rootTable.add(tableLeft);
        rootTable.add(tableRight);

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
