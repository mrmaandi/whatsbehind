package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Main screen for the game.
 */
public class GameScreen implements Screen {

    private final WhatBehindTheDoorGame game;
    private final Stage stage;

    Label labelLevel;
    Table rootTable;

    Label hostLabel;
    String hostText = "Hello player and welcome to my game show!\n\nI'll walk you through how to play.";
    String hostText2 = "You have 3 doors.\nBehind two doors are things which hurt you.\nOne of the doors holds a reward.";
    int hostTextPosition = 0;

    ArrayList<String> hostIntro = new ArrayList<>();
    // If you're familiar with Monty Hall Problem then we'll get started!
    //
    //You can go to main menu to get more info.
    float hostTextTimer = 0f;
    int currentCharacterPosition = 0;


    public GameScreen(final WhatBehindTheDoorGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        rootTable = new Table(game.skin);
        rootTable.debugAll();
        rootTable.background("window");
        rootTable.setFillParent(true);

        hostIntro.add(hostText);
        hostIntro.add(hostText2);

        // Create UI elements
        //rootTable.row().expandX().fill();
        // Top section
        // Left pane
        Table tableLeft = new Table();
        tableLeft.row().expandX().fillX().expandY().fillY();
        Window window = new Window("", game.skin, "dialog");
        //window.debugAll();
        window.setMovable(false);
        window.getTitleTable().add(new Label("Game board", game.skin, "title")).expandX().left();
        window.row().height(40);
        Label label = new Label("Pick a door", game.skin);
        window.add(label);

        window.row(); // Door options
        Button option1 = new TextButton("\"\"\"\"\"\"\"\n\n\n\n\nLEFT\n\n\n\n\n.......", game.skin);
        option1.addListener(doorClickEvent(0));
        Button option2 = new TextButton("\"\"\"\"\"\"\"\n\n\n\n\nMIDDLE\n\n\n\n\n.......", game.skin);
        option2.addListener(doorClickEvent(1));
        Button option3 = new TextButton("\"\"\"\"\"\"\"\n\n\n\n\nRIGHT\n\n\n\n\n.......", game.skin);
        option3.addListener(doorClickEvent(2));
        HorizontalGroup group = new HorizontalGroup();
        String wallLabelText = "_|___|__\n" +
            "___|___|\n" +
            "_|___|__\n" +
            "___|___|\n" +
            "_|___|__\n" +
            "___|___|\n" +
            "_|___|__\n" +
            "___|___|\n" +
            "_|___|__\n" +
            "___|___|\n" +
            "_|___|__\n" +
            "___|___|";
        group.addActor(new Label(wallLabelText, game.skin));
        group.space(10);
        group.addActor(option1);
        option1.addAction(sequence(moveBy(0, 30), parallel(fadeIn(2), moveBy(0, -30, 2, Interpolation.bounceOut))));
        group.addActor(option2);
        option2.addAction(sequence(moveBy(0, 30), delay(0.2f), parallel(fadeIn(2), moveBy(0, -30, 2, Interpolation.bounceOut))));
        group.addActor(option3);
        option3.addAction(sequence(moveBy(0, 30), delay(0.4f), parallel(fadeIn(2), moveBy(0, -30, 2, Interpolation.bounceOut))));
        group.addActor(new Label(wallLabelText, game.skin));
        window.add(group);

        window.row().height(40); // Player choice label
        Label playerChoiceLabel = new Label("", game.skin);
        ChangeListener playerChoiceLabelListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Label1 event");
                String playerChoiceLabelText = "You chose door: " + game.gameState.playerChoice;
                playerChoiceLabel.setText(playerChoiceLabelText);
            }
        };
        game.eventManager.addChangeListener(playerChoiceLabelListener);
        window.add(playerChoiceLabel);

        window.row(); // Confirm area
        Button confirmButton = new TextButton("Confirm", game.skin);
        window.add(confirmButton);
        tableLeft.add(window);

        // Left pane - Bottom section
        tableLeft.row().expandX().fillX().bottom();
        labelLevel = new Label(getStatsLabel(), game.skin);
        ChangeListener labelListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Label2 event");
                labelLevel.setText(getStatsLabel());
            }
        };
        labelLevel.addListener(labelListener);
        game.eventManager.addChangeListener(labelListener);
        tableLeft.add(labelLevel);

        // Right pane
        Table tableRight = new Table();
        //tableRight.debugAll();
        Window hostWindow = new Window("", game.skin, "dialog");
        //hostWindow.debugAll();
        hostWindow.getTitleTable().add(new Label("Host", game.skin, "title")).expandX().left();
        hostWindow.setMovable(false);
        hostWindow.add(new Label(
            "    .-\"\"\"\"\"\"-.\n" +
                "  .'  \\\\  //  '.\n" +
                " /   O      O   \\\n" +
                ":                :\n" +
                "|                |\n" +
                ":       __       :\n" +
                " \\  .-\"`  `\"-.  /\n" +
                "  '.          .'\n" +
                "    '-......-'", game.skin));
        hostWindow.row().expand().fill().pad(30, 5, 30, 5);
        hostLabel = new Label("", game.skin);
        hostLabel.setWrap(true);
        hostWindow.add(hostLabel);
        hostWindow.row();
        Button nextButton = new TextButton("Next", game.skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click next");
                hostTextPosition++;
                hostTextTimer = 0f;
                currentCharacterPosition = 0;
                if (hostTextPosition == hostIntro.size()-1) {
                    hostWindow.removeActor(nextButton);
                }
            }
        });
        hostWindow.add(nextButton);
        tableRight.add(hostWindow).fillX().expandX();
        //tableRight.row().fill().expand();
        //Label labelRight = new Label("Inventory:\n\n1 Hammer\n0 Bullet\n2 Magnifier\n1 Medpack", game.skin);
        //tableRight.add(labelRight);

        rootTable.add(tableLeft);
        rootTable.add(tableRight);

        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
    }

    private String getStatsLabel() {
        return "Level: " + game.gameState.level + "   " + "Coins: " + game.gameState.coins + "   " + "Health: " + game.gameState.playerHealth + "/" + game.gameState.maxPlayerHealth;
    }

    @Override
    public void show() {
        // Prepare your screen here.
        // Create table
    }

    private ClickListener doorClickEvent(Integer playerChoice) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click event");
                game.gameState.playerChoice = playerChoice;
                game.eventManager.receiveStateUpdate();
            }
        };
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        ScreenUtils.clear(0, 0, 0, 1);

        hostSpeak(delta);

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.mainMenuScreen);
            // dispose();
        }
    }

    private void hostSpeak(float delta) {
        String currentText = hostIntro.get(hostTextPosition);
        if (currentCharacterPosition == currentText.length()) {
            return;
        }
        System.out.println(delta);
        hostTextTimer += delta;
        if (hostTextTimer < 0.05f) {
            return;
        }
        currentCharacterPosition++;
        hostLabel.setText(currentText.substring(0, currentCharacterPosition));
        hostTextTimer = 0f;
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
