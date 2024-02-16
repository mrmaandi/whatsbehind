package com.gamejam;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameState {

  Integer playerChoice = null;
  Integer winningOption = null;
  Integer level = null;
  Integer playerHealth = null;
  Integer maxPlayerHealth = null;
  Integer coins = null;
  Boolean playerChoiceConfirmed = null;
  // Boolean playerChoiceSecondConfirmed = null;
  Integer revealedDoor = null;
  Boolean revealedDoorAnimationPlayed = null;
  Boolean readyForShop = null;
  Boolean showHostNextButton = null;
  Boolean showShop = null;

  // Host
  List<String> hostIntro =
      Arrays.asList(
          "Hello player and welcome to my game show!\n\nI'll walk you through how to play.",
          "In front of you are 3 doors.\n\nBut only one holds a reward.",
          "Remember:\n\nhigh risk = high reward");

  List<String> hostCurrentSpeak = hostIntro;
  int hostTextPosition = 0;

  float hostTextTimer = 0f;
  int currentCharacterPosition = 0;
  String currentHostText = null;

  public void setHostCurrentSpeak(List<String> texts) {
    hostTextPosition = 0;
    hostTextTimer = 0f;
    currentCharacterPosition = 0;
    hostCurrentSpeak = texts;
    showHostNextButton = true;
  }

  public void loadNextLevel() {
    level++;
    playerChoiceConfirmed = false;
    // playerChoice = null;
    readyForShop = false;
    revealedDoor = null;
    revealedDoorAnimationPlayed = false;
    winningOption = generateWinningOption();
    playerChoice = null;
    showShop = false;
    setHostCurrentSpeak(Arrays.asList("Lets play again. Choose one door"));
    System.out.println("winning option: " + winningOption);
  }

  public int generateWinningOption() {
    Random random = new Random();
    return random.nextInt(3);
  }

  @Override
  public String toString() {
    return "GameState{"
        + "playerChoice="
        + playerChoice
        + ", winningOption="
        + winningOption
        + ", level="
        + level
        + ", playerHealth="
        + playerHealth
        + ", maxPlayerHealth="
        + maxPlayerHealth
        + ", coins="
        + coins
        + ", playerChoiceConfirmed="
        + playerChoiceConfirmed
        + ", revealedDoor="
        + revealedDoor
        + ", readyForShop="
        + readyForShop
        + ", hostIntro="
        + hostIntro
        + ", hostCurrentSpeak="
        + hostCurrentSpeak
        + ", hostTextPosition="
        + hostTextPosition
        + ", hostTextTimer="
        + hostTextTimer
        + ", currentCharacterPosition="
        + currentCharacterPosition
        + ", currentHostText='"
        + currentHostText
        + '\''
        + '}';
  }
}
