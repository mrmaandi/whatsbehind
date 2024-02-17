package com.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import text.formic.Stringf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/** Main screen for the game. */
public class GameScreen implements Screen {

  private static final String HOST_FACE_HAPPY =
      "    .-\"\"\"\"\"\"-.\n"
          + "  .'          '.\n"
          + " /   O      O   \\\n"
          + ":                :\n"
          + "|                |\n"
          + ": ',          ,' :\n"
          + " \\  '-......-'  /\n"
          + "  '.          .'\n"
          + "    '-......-'";
  private static final String HOST_FACE_ANGRY =
      "    .-\"\"\"\"\"\"-.\n"
          + "  .'  \\\\  //  '.\n"
          + " /   O      O   \\\n"
          + ":                :\n"
          + "|                |\n"
          + ":       __       :\n"
          + " \\  .-\"`  `\"-.  /\n"
          + "  '.          .'\n"
          + "    '-......-'";
  private static final String HOST_FACE_GOOFY =
      " , ; ,   .-'\"\"\"'-.   , ; ,\n"
          + " \\\\|/  .'         '.  \\|//\n"
          + "  \\-;-/   ()   ()   \\-;-/\n"
          + "  // ;               ; \\\\\n"
          + " //__; :.         .; ;__\\\\\n"
          + "`-----\\'.'-.....-'.'/-----'\n"
          + "       '.'.-.-,_.'.'\n"
          + "         '(  (..-'\n"
          + "           '-'";
  private static final String HOST_FACE_DEAD =
      "    .-\"\"\"\"\"\"-.\n"
          + "  .'          '.\n"
          + " /   X      X   \\\n"
          + ":           `    :\n"
          + "|                |\n"
          + ":    .------.    :\n"
          + " \\  '        '  /\n"
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
  Table switchableTable;

  Table tableGameBoard;
  ProgressBar healthBar;

  Label hostFaceLabel;

  ArrayList<Sound> typeSounds;
  Sound clickSound;
  Sound hitHurtSound;
  Sound coinSound;
  Sound explosionSound;

  public GameScreen(final WhatBehindTheDoorGame game) {
    this.game = game;
    stage = new Stage(new FitViewport(1280, 720));
    typeSounds = new ArrayList<>();

    // Load sounds
    Sound typeSound = game.assetManager.get("sound/type1.wav", Sound.class);
    typeSounds.add(typeSound);
    Sound typeSound2 = game.assetManager.get("sound/type2.wav", Sound.class);
    typeSounds.add(typeSound2);
    clickSound = game.assetManager.get("sound/click.wav", Sound.class);
    hitHurtSound = game.assetManager.get("sound/hitHurt.wav", Sound.class);
    coinSound = game.assetManager.get("sound/coin.wav", Sound.class);
    explosionSound = game.assetManager.get("sound/explosion.wav", Sound.class);

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
    Table tableHost = createHostSideArea();
    rootTable.row().expandY().fillY();
    rootTable.add(tableHost);

    switchableTable = new Table(game.skin);
    switchableTable.row().expand().fill();
    gameWindow = createWindowPickDoorsContent();
    shopWindow = createWindowShopContent();

    stage.addActor(rootTable);
  }

  private Table createGameSideArea() {
    tableGameBoard = new Table();
    tableGameBoard.row().expand().fill();

    switchableTable.add(gameWindow);
    tableGameBoard.add(switchableTable);

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
    window.add(new Label("Purchase items:\n", game.skin));
    window.row().height(TEXT_BUTTON_HEIGHT);

    // Med pack
    HorizontalGroup group = new HorizontalGroup();
    group.space(10);
    TextButton medPackButton = new TextButton("Buy", game.skin);
    medPackButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            int itemCost = 1;
            if (game.gameState.coins < itemCost) {
              return;
            }
            if (Objects.equals(game.gameState.playerHealth, game.gameState.maxPlayerHealth)) {
              return;
            }

            game.gameState.coins = game.gameState.coins - itemCost;
            game.gameState.playerHealth++;
          }
        });
    Label medPackItemLabel = new Label("MEDPACK (Cost: 1)", game.skin);
    group.addActor(medPackItemLabel);
    group.addActor(medPackButton);
    window.add(group);
    window.row();
    Label medPackInfoLabel = new Label("Heals the player by 1 life.", game.skin, "error");
    window.add(medPackInfoLabel);

    // Bomb
    HorizontalGroup bombItemGroup = new HorizontalGroup();
    bombItemGroup.space(10);
    TextButton bombItemButton = new TextButton("Buy", game.skin);
    bombItemButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            int itemCost = 2;
            if (game.gameState.coins < itemCost) {
              return;
            }

            game.gameState.coins = game.gameState.coins - itemCost;
            game.gameState.hitHost();
            hitHurtSound.play();
            hostFaceLabel.setText(HOST_FACE_ANGRY);
            game.gameState.setHostCurrentSpeak(Arrays.asList("That was painful!"));
            healthBar.setValue(game.gameState.hostCurrentHealth);
            if (game.gameState.hostCurrentHealth == 0) {
              System.out.println("PLAYER WINS!");
              hostFaceLabel.setText(HOST_FACE_DEAD);
              game.gameState.setHostCurrentSpeak(
                  Arrays.asList("Congratulations - You won! Thanks for playing!"));
              switchableTable.clear();

              switchableTable.row().expand().fill();

              Window playAgainWindow = new Window("", game.skin, "dialog");
              TextButton restartButton = new TextButton("Play again", game.skin);
              restartButton.addListener(
                  new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                      System.out.println("Restart game clicked");
                      switchableTable.clear();
                      switchableTable.row().expand().fill();
                      switchableTable.add(gameWindow);
                      showDoorsWithAnimation();
                      game.gameState.resetState();
                      hostFaceLabel.setText(HOST_FACE_HAPPY);
                      healthBar.setValue(game.gameState.hostMaxHealth);
                    }
                  });
              playAgainWindow.add(restartButton).height(TEXT_BUTTON_HEIGHT);
              switchableTable.add(playAgainWindow);
            }
            game.eventManager.sendStateUpdate();
          }
        });
    window.row().height(TEXT_BUTTON_HEIGHT).padTop(20);
    Label bombItemLabel = new Label("BOMB (Cost: 2)", game.skin);
    bombItemGroup.addActor(bombItemLabel);
    bombItemGroup.addActor(bombItemButton);
    window.add(bombItemGroup);
    window.row();
    Label bombItemInfoLabel = new Label("Deals random damage to host.", game.skin, "error");
    window.add(bombItemInfoLabel);

    window.row().height(TEXT_BUTTON_HEIGHT).padTop(100);
    TextButton nextButton = new TextButton("Continue", game.skin);
    nextButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            switchableTable.clear();
            switchableTable.row().expand().fill();
            switchableTable.add(gameWindow);
            hostFaceLabel.setText(HOST_FACE_HAPPY);
            game.gameState.setHostCurrentSpeak(Arrays.asList("Lets play again. Choose one door"));
            game.gameState.loadNextLevel();
            showDoorsWithAnimation();
          }
        });
    window.add(nextButton);
    return window;
  }

  private Window createWindowPickDoorsContent() {
    Window window = new Window("", game.skin, "dialog");
    window.setMovable(false);
    window.getTitleTable().add(new Label("Game board", game.skin, "title")).expandX().left();

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
        sequence(
            moveBy(0, 30), parallel(fadeIn(0.5f), moveBy(0, -30, 1, Interpolation.bounceOut))));
    group.addActor(option2);
    option2.addAction(
        sequence(
            moveBy(0, 30),
            delay(0.2f),
            parallel(fadeIn(0.5f), moveBy(0, -30, 1, Interpolation.bounceOut))));
    group.addActor(option3);
    option3.addAction(
        sequence(
            moveBy(0, 30),
            delay(0.4f),
            parallel(fadeIn(0.5f), moveBy(0, -30, 1, Interpolation.bounceOut))));
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
                      Stringf.format(
                          "I will reveal that door %s has no reward.",
                          game.gameState.revealedDoor + 1),
                      "You may now choose to STAY or SWITCH."));
              game.eventManager.sendStateUpdate();
              return;
            }

            boolean secondConfirm = !game.gameState.readyForShop;
            if (secondConfirm) {
              System.out.println("> second confirm");
              if (game.gameState.winningOption == game.gameState.playerChoice) {
                game.gameState.setHostCurrentSpeak(
                    Arrays.asList("You picked the door with reward!\n\nYou earn 1 coin!"));
                coinSound.play();
                game.gameState.coins++;
              } else {
                int healAmount = game.gameState.healHost();
                game.gameState.setHostCurrentSpeak(
                    Arrays.asList(
                        Stringf.format(
                            "Your door does not have any prize!\n\nYou lose one life.\n\nI also heal for %s.",
                            healAmount)));
                hitHurtSound.play();
                healthBar.setValue(game.gameState.hostCurrentHealth);
                game.gameState.playerHealth--;
              }

              // fade out door without reward
              int revealedDoor = game.gameState.revealedDoor;
              int winningOption = game.gameState.winningOption;
              TextButton button;
              if (revealedDoor != 0 && winningOption != 0) {
                button = option1;
              } else if (revealedDoor != 1 && winningOption != 1) {
                button = option2;
              } else {
                button = option3;
              }

              button.addAction(alpha(0.2f, 0.5f));

              game.gameState.readyForShop = true;
              game.eventManager.sendStateUpdate();
              return;
            }

            // last confirm
            System.out.println("> Last confirm");

            if (game.gameState.playerHealth == 0) {
              System.out.println("PLAYER DIED!");
              game.gameState.setHostCurrentSpeak(
                  Arrays.asList("You died - Game over! Thanks for playing!"));
              hostFaceLabel.setText(HOST_FACE_GOOFY);
              explosionSound.play();
              switchableTable.clear();
              switchableTable.row().expand().fill();

              Window playAgainWindow = new Window("", game.skin, "dialog");
              TextButton restartButton = new TextButton("Play again", game.skin);
              restartButton.addListener(
                  new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                      System.out.println("Restart game clicked");
                      switchableTable.clear();
                      switchableTable.row().expand().fill();
                      switchableTable.add(gameWindow);
                      showDoorsWithAnimation();
                      game.gameState.resetState();
                      hostFaceLabel.setText(HOST_FACE_HAPPY);
                      healthBar.setValue(game.gameState.hostMaxHealth);
                    }
                  });
              playAgainWindow.add(restartButton).height(TEXT_BUTTON_HEIGHT);
              switchableTable.add(playAgainWindow);
              return;
            }

            // Confirm - Go to shop
            game.gameState.showShop = true;

            switchableTable.clear();
            switchableTable.row().expand().fill();
            switchableTable.add(shopWindow);
            game.gameState.setHostCurrentSpeak(
                Arrays.asList(
                    "Welcome to my shop! You can purchase anything if you have the coins.\n\nItems will be instantly used upon purchase."));

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

  private void showDoorsWithAnimation() {
    option1.addAction(alpha(1f, 1, Interpolation.swingIn));
    option2.addAction(alpha(1f, 1, Interpolation.swingIn));
    option3.addAction(alpha(1f, 1, Interpolation.swingIn));
  }

  private String getDoorText(String doorName, boolean isSelected) {
    String doorText;
    if (isSelected) {
      doorText = ">" + doorName + "<";
    } else {
      doorText = doorName;
    }
    return Stringf.format(DOOR_TEXT_BASE, doorText);
  }

  private Table createHostSideArea() {
    Table tableRight = new Table();
    tableRight.row().expandY().fillY();
    // tableRight.debugAll();
    Window hostWindow = new Window("", game.skin, "dialog");
    // hostWindow.debugAll();
    hostWindow.getTitleTable().add(new Label("Host", game.skin, "title")).expandX().left();
    hostWindow.setMovable(false);
    hostFaceLabel = new Label(HOST_FACE_HAPPY, game.skin);
    hostWindow.add(hostFaceLabel);

    hostWindow.row().bottom().padTop(20);
    Label hostHealthLabel = new Label("", game.skin);
    ChangeListener hostHealthLabelChangeListener =
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            hostHealthLabel.setText(
                Stringf.format(
                    "Health (%s/%s)",
                    game.gameState.hostCurrentHealth, game.gameState.hostMaxHealth));
          }
        };
    hostHealthLabel.addListener(hostHealthLabelChangeListener);
    game.eventManager.addChangeListener(hostHealthLabelChangeListener);

    hostWindow.add(hostHealthLabel);

    hostWindow.row().pad(10).expandX().fillX();
    healthBar = new ProgressBar(0, 100, 1, false, game.skin);
    healthBar.setValue(game.gameState.hostCurrentHealth);
    healthBar.setAnimateDuration(1f);
    healthBar.setAnimateInterpolation(Interpolation.elastic);
    hostWindow.add(healthBar);
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
    hostWindow.row().height(TEXT_BUTTON_HEIGHT).padBottom(10);
    nextButton = new TextButton("Next", game.skin);
    nextButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            System.out.println("Click next");
            if (tableLeft == null) { // First next click!
              tableLeft = createGameSideArea();
              rootTable.add(tableLeft).expandX().fillX();
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
    return Stringf.format(
        "Health: %s/%s  Coins: %s  Round: %s",
        game.gameState.playerHealth,
        game.gameState.maxPlayerHealth,
        game.gameState.coins,
        game.gameState.level);
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
              button.addAction(alpha(0.2f, 0.5f));
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
    ArrayList<Integer> goatDoors = new ArrayList<>();
    if (game.gameState.winningOption != 0 && game.gameState.playerChoice != 0) {
      goatDoors.add(0);
    }
    if (game.gameState.winningOption != 1 && game.gameState.playerChoice != 1) {
      goatDoors.add(1);
    }
    if (game.gameState.winningOption != 2 && game.gameState.playerChoice != 2) {
      goatDoors.add(2);
    }

    int max = goatDoors.size();
    game.gameState.revealedDoor = goatDoors.get(new Random().nextInt(max));
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
        clickSound.play();
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
    if (game.gameState.hostTextTimer < 0.07f) {
      return;
    }
    game.gameState.currentCharacterPosition++;
    game.gameState.currentHostText =
        currentText.substring(0, game.gameState.currentCharacterPosition);
    game.gameState.hostTextTimer = 0f;
    game.eventManager.sendStateUpdate();
    if (game.gameState.currentCharacterPosition % 2 == 0) {
      typeSounds.get(new Random().nextInt(typeSounds.size())).play();
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
