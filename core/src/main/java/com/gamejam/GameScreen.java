package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/** Main screen for the game. */
public class GameScreen implements Screen {

  private static final String HOST_FACE =
      "    .-\"\"\"\"\"\"-.\n"
          + "  .'  \\\\  //  '.\n"
          + " /   O      O   \\\n"
          + ":                :\n"
          + "|                |\n"
          + ":       __       :\n"
          + " \\  .-\"`  `\"-.  /\n"
          + "  '.          .'\n"
          + "    '-......-'";
  private static final String WALL_LABEL_TEXT =
      "_|___|__\n"
          + "___|___|\n"
          + "_|___|__\n"
          + "___|___|\n"
          + "_|___|__\n"
          + "___|___|\n"
          + "_|___|__\n"
          + "___|___|\n"
          + "_|___|__\n"
          + "___|___|\n"
          + "_|___|__\n"
          + "___|___|";
  private static final String DOOR_TEXT_BASE = "\"\"\"\"\"\"\"\"\n\n\n\n\n%s\n\n\n\n\n........";
  private static final int TEXT_BUTTON_HEIGHT = 40;

  private final WhatBehindTheDoorGame game;
  private final Stage stage;
  Label labelLevel;
  Table rootTable;
  Label hostLabel;
  Table tableLeft;
  TextButton nextButton;

  TextButton option1;
  TextButton option2;
  TextButton option3;

  Window gameWindow;
  Window shopWindow;

  Table tableGameBoard;

  public GameScreen(final WhatBehindTheDoorGame game) {
    this.game = game;
    stage = new Stage(new ScreenViewport());

    stage.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            // GO over every label , button... and update dem
            System.out.println("Main update");
          }
        });

    rootTable = new Table(game.skin);
    rootTable.row().expand().fill();
    // rootTable.debugAll();
    rootTable.background("window");
    rootTable.setFillParent(true);

    // Create UI elements
    Table tableHost = createHostTable();
    rootTable.row().expand().fill();
    rootTable.add(tableHost);

    gameWindow = createWindowPickDoorsContent();
    shopWindow = createWindowShopContent();

    stage.addActor(rootTable);
  }

  private Table createGameTable() {
    tableGameBoard = new Table();
    tableGameBoard.row().expand().fill();

    tableGameBoard.add(gameWindow);

    // Left pane - Bottom section
    tableGameBoard.row().height(TEXT_BUTTON_HEIGHT);
    labelLevel = new Label(getStatsLabel(), game.skin);
    ChangeListener labelListener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            labelLevel.setText(getStatsLabel());
          }
        };
    labelLevel.addListener(labelListener);
    game.eventManager.addChangeListener(labelListener);
    tableGameBoard.add(labelLevel);
    return tableGameBoard;
  }

  private Window createWindowShopContent() {
    Window window = new Window("", game.skin, "dialog");
    window.setMovable(false);
    window.getTitleTable().add(new Label("Shop", game.skin, "title")).expandX().left();
    window.row();
    window.add(new Label("now u in shop", game.skin));

    window.row().height(TEXT_BUTTON_HEIGHT);
    TextButton nextButton = new TextButton("Continue", game.skin);
    nextButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            tableGameBoard.removeActor(shopWindow);
            tableGameBoard.add(gameWindow);
            game.gameState.loadNextLevel();
          }
        });
    window.add(nextButton);
    return window;
  }

  private Window createWindowPickDoorsContent() {
    Window window = new Window("", game.skin, "dialog");
    window.setMovable(false);
    window.getTitleTable().add(new Label("Game board", game.skin, "title")).expandX().left();
    ChangeListener gameWindowListener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            if (game.gameState.showShop) {
              System.out.println("SHOW SHOP");
              // set to shop window
              tableGameBoard.removeActor(gameWindow);
              tableGameBoard.add(shopWindow);
            }
          }
        };
    window.addListener(gameWindowListener);
    game.eventManager.addChangeListener(gameWindowListener);
    window.row().height(200);
    Label label = new Label("Pick a door", game.skin);
    window.add(label);

    window.row(); // Door options
    option1 = new TextButton(getDoorText("LEFT", false), game.skin);
    option1.addListener(createDoorClickListener(0));
    option1.addListener(createDoorChangeListener(0, "LEFT", option1));
    option2 = new TextButton(getDoorText("MIDDLE", false), game.skin);
    option2.addListener(createDoorClickListener(1));
    option2.addListener(createDoorChangeListener(1, "MIDDLE", option2));
    option3 = new TextButton(getDoorText("RIGHT", false), game.skin);
    option3.addListener(createDoorClickListener(2));
    option3.addListener(createDoorChangeListener(2, "RIGHT", option3));
    HorizontalGroup group = new HorizontalGroup();

    group.addActor(new Label(WALL_LABEL_TEXT, game.skin));
    group.space(10);
    group.addActor(option1);
    option1.addAction(
        sequence(moveBy(0, 30), parallel(fadeIn(2), moveBy(0, -30, 2, Interpolation.bounceOut))));
    group.addActor(option2);
    option2.addAction(
        sequence(
            moveBy(0, 30),
            delay(0.2f),
            parallel(fadeIn(2), moveBy(0, -30, 2, Interpolation.bounceOut))));
    group.addActor(option3);
    option3.addAction(
        sequence(
            moveBy(0, 30),
            delay(0.4f),
            parallel(fadeIn(2), moveBy(0, -30, 2, Interpolation.bounceOut))));
    group.addActor(new Label(WALL_LABEL_TEXT, game.skin));
    window.add(group);

    window.row().padTop(50); // Player choice label
    Label playerChoiceLabel = new Label("", game.skin);
    ChangeListener playerChoiceLabelListener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            String playerChoiceLabelText = "You chose door: " + game.gameState.playerChoice;
            playerChoiceLabel.setText(playerChoiceLabelText);
          }
        };
    game.eventManager.addChangeListener(playerChoiceLabelListener);
    // window.add(playerChoiceLabel);

    window.row().height(TEXT_BUTTON_HEIGHT).padBottom(50); // Confirm area
    Button confirmButton = new TextButton("Continue", game.skin);
    confirmButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            System.out.println("Click confirm");

            if (confirmButton.isDisabled()) {
              return;
            }

            boolean firstConfirm = !game.gameState.playerChoiceConfirmed;
            if (firstConfirm) {
              System.out.println("> first confirm");
              revealBadDoor();
              game.gameState.playerChoiceConfirmed = true;
              game.gameState.setHostCurrentSpeak(
                  Arrays.asList(
                      "You clicked and confirmed selection!",
                      "Now I will reveal a door, which has no prize",
                      String.format(
                          "Door %s has no reward.\n\nYou may now choose whether you want to STAY or SWITCH.\n\nMake your choice carefully.",
                          game.gameState.revealedDoor + 1)));
              game.eventManager.sendStateUpdate();
              return;
            }

            boolean secondConfirm = !game.gameState.readyForShop;
            if (secondConfirm) {
              System.out.println("> second confirm");
              if (game.gameState.winningOption == game.gameState.playerChoice) {
                game.gameState.setHostCurrentSpeak(
                    Arrays.asList(
                        "Congratulations! You picked winning option and thereby earn 1 coin!"));
                game.gameState.coins++;
              } else {
                game.gameState.setHostCurrentSpeak(Arrays.asList("I WIN! You lose life!!"));
                game.gameState.playerHealth--;
              }
              game.gameState.readyForShop = true;
              game.eventManager.sendStateUpdate();
              return;
            }

            // last confirm
            System.out.println("> Last confirm");

            if (game.gameState.playerHealth == 0) {
              System.out.println("PLAYER DIED!");
              game.gameState.setHostCurrentSpeak(Arrays.asList("You died!!! Game over"));
              return;
            }

            // Load next level
            game.gameState.showShop = true;

            /* game.gameState.loadNextLevel();
            option1.addAction(alpha(1f, 1, Interpolation.swingIn));
            option2.addAction(alpha(1f, 1, Interpolation.swingIn));
            option3.addAction(alpha(1f, 1, Interpolation.swingIn));*/

            game.eventManager.sendStateUpdate();
          }
        });
    ChangeListener confirmButtonChangeListener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            if (game.gameState.playerChoice == null) {
              confirmButton.setDisabled(true);
            } else {
              confirmButton.setDisabled(false);
            }
          }
        };
    confirmButton.addListener(confirmButtonChangeListener);
    game.eventManager.addChangeListener(confirmButtonChangeListener);
    window.add(confirmButton);
    return window;
  }

  private String getDoorText(String doorName, boolean isSelected) {
    String doorText;
    if (isSelected) {
      doorText = ">" + doorName + "<";
    } else {
      doorText = doorName;
    }
    return String.format(DOOR_TEXT_BASE, doorText);
  }

  private Table createHostTable() {
    Table tableRight = new Table();
    tableRight.row().expand().fill();
    // tableRight.debugAll();
    Window hostWindow = new Window("", game.skin, "dialog");
    // hostWindow.debugAll();
    hostWindow.getTitleTable().add(new Label("Host", game.skin, "title")).expandX().left();
    hostWindow.setMovable(false);
    Label hostFaceLabel = new Label(HOST_FACE, game.skin);
    hostWindow.add(hostFaceLabel);
    hostWindow.row().expand().fill().pad(30, 5, 30, 5);
    hostLabel = new Label("", game.skin);
    hostLabel.setWrap(true);
    ChangeListener listener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            hostLabel.setText(game.gameState.currentHostText);
          }
        };
    hostLabel.addListener(listener);
    game.eventManager.addChangeListener(listener);
    hostWindow.add(hostLabel);
    hostWindow.row().height(TEXT_BUTTON_HEIGHT);
    nextButton = new TextButton("Next", game.skin);
    nextButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            System.out.println("Click next");
            if (tableLeft == null) { // First next click!
              tableLeft = createGameTable();
              rootTable.add(tableLeft);
              tableLeft.addAction(sequence(fadeOut(0), fadeIn(1)));
            }

            if (game.gameState.hostTextPosition == game.gameState.hostCurrentSpeak.size() - 1) {
              return;
            }

            game.gameState.hostTextPosition++;
            game.gameState.hostTextTimer = 0f;
            game.gameState.currentCharacterPosition = 0;

            // hide next button when last text comes
            if (game.gameState.hostTextPosition == game.gameState.hostCurrentSpeak.size() - 1) {
              game.gameState.showHostNextButton = false;
            }
          }
        });
    ChangeListener nextButtonChangeListener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            if (!game.gameState.showHostNextButton) {
              nextButton.addAction(Actions.hide());
            } else {
              nextButton.addAction(Actions.show());
            }
          }
        };
    nextButton.addListener(nextButtonChangeListener);
    game.eventManager.addChangeListener(nextButtonChangeListener);
    hostWindow.add(nextButton);
    tableRight.add(hostWindow);
    tableRight.row().expandX().fillX().height(TEXT_BUTTON_HEIGHT);
    TextButton mainMenuButton = new TextButton("MENU", game.skin);
    mainMenuButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            game.setScreen(game.mainMenuScreen);
          }
        });

    /*Label labelRight = new Label("Inventory:\n\n1 Hammer\n0 Bullet\n2 Magnifier\n1 Medpack", game.skin);*/
    tableRight.add(mainMenuButton).left();
    return tableRight;
  }

  private String getStatsLabel() {
    return "Round: "
        + game.gameState.level
        + "   "
        + "Coins: "
        + game.gameState.coins
        + "   "
        + "Health: "
        + game.gameState.playerHealth
        + "/"
        + game.gameState.maxPlayerHealth;
  }

  @Override
  public void show() {
    // Prepare your screen here.
    Gdx.input.setInputProcessor(stage);
    stage.getRoot().getColor().a = 0;
    stage.getRoot().addAction(fadeIn(0.5f));
  }

  private ChangeListener createDoorChangeListener(
      int doorIndex, String doorName, TextButton button) {
    ChangeListener listener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {

            boolean isHostRevealedDoor =
                game.gameState.revealedDoor != null
                    && game.gameState.revealedDoor == doorIndex
                    && !game.gameState.revealedDoorAnimationPlayed;
            if (isHostRevealedDoor) {
              System.out.println("is host revel door");
              button.addAction(alpha(0.2f, 0.5f, Interpolation.swingIn));
              game.gameState.revealedDoorAnimationPlayed = true;
            }

            boolean isSelected =
                game.gameState.playerChoice != null && doorIndex == game.gameState.playerChoice;
            String text = getDoorText(doorName, isSelected);
            button.getLabel().setText(text);
          }
        };
    game.eventManager.addChangeListener(listener);
    return listener;
  }

  private void revealBadDoor() {
    System.out.println("reveal bad door");
    int goatDoor;
    if (game.gameState.winningOption != 0 && game.gameState.playerChoice != 0) {
      goatDoor = 0;
    } else if (game.gameState.winningOption != 1 && game.gameState.playerChoice != 1) {
      goatDoor = 1;
    } else {
      goatDoor = 2;
    }

    game.gameState.revealedDoor = goatDoor;
    game.eventManager.sendStateUpdate();
  }

  private ClickListener createDoorClickListener(Integer doorIndex) {
    return new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        System.out.println("Click event");
        if (game.gameState.revealedDoor == doorIndex) {
          return;
        }
        game.gameState.playerChoice = doorIndex;
        game.eventManager.sendStateUpdate();
      }
    };
  }

  @Override
  public void render(float delta) {
    // Draw your screen here. "delta" is the time since last render in seconds.
    recalculateHostSpeak(delta);

    stage.act(delta);
    stage.draw();
  }

  private void recalculateHostSpeak(float delta) {
    String currentText = game.gameState.hostCurrentSpeak.get(game.gameState.hostTextPosition);
    if (game.gameState.currentCharacterPosition == currentText.length()) {
      return;
    }
    game.gameState.hostTextTimer += delta;
    if (game.gameState.hostTextTimer < 0.05f) {
      return;
    }
    game.gameState.currentCharacterPosition++;
    game.gameState.currentHostText =
        currentText.substring(0, game.gameState.currentCharacterPosition);
    game.gameState.hostTextTimer = 0f;
    game.eventManager.sendStateUpdate();
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
