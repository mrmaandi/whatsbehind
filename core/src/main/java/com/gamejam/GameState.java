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
  Integer revealedDoor = null;
  Boolean revealedDoorAnimationPlayed = null;
  Boolean readyForShop = null;
  Boolean showHostNextButton = null;
  Boolean showShop = null;
  Integer hostCurrentHealth = null;
  Integer hostMaxHealth = null;

  Integer itemsMedPack = null;

  // Host
  List<String> hostIntro =
      Arrays.asList(
          "Hello player and welcome to my game show!\n\nI'll walk you through how to play.",
          "In front of you are 3 doors.\n\nBut only one holds a reward.",
          "Pick one which you think will have the reward and press continue.");

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
    boolean showNext = texts.size() > 1;
    showHostNextButton = showNext;
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
    System.out.println("winning option: " + winningOption);
  }

  public void resetState() {
    level = 1;
    maxPlayerHealth = 3;
    playerHealth = maxPlayerHealth;
    playerChoiceConfirmed = false;
    playerChoice = null;
    coins = 0;
    readyForShop = false;
    winningOption = generateWinningOption();
    showHostNextButton = true;
    revealedDoor = null;
    revealedDoorAnimationPlayed = false;
    showShop = false;
    itemsMedPack = 0;
    hostMaxHealth = 100;
    hostCurrentHealth = hostMaxHealth;

    setHostCurrentSpeak(hostIntro);
  }

  public int hitHost() {
    int randomInt = new Random().nextInt(40) + 10;

    int hitAmount = Math.min(randomInt, hostCurrentHealth);
    hostCurrentHealth = hostCurrentHealth - hitAmount;
    return hitAmount;
  }

  public int healHost() {
    int maxAllowedHeal = hostMaxHealth - hostCurrentHealth;
    int randomInt = new Random().nextInt(20) + 10;

    int healAmount = Math.min(randomInt, maxAllowedHeal);
    hostCurrentHealth = hostCurrentHealth + healAmount;
    return healAmount;
  }

  public int generateWinningOption() {
    Random random = new Random();
    return random.nextInt(3);
  }
}
