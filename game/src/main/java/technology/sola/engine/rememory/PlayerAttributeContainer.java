package technology.sola.engine.rememory;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.events.ForgetEverythingEvent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

public class PlayerAttributeContainer {
  private int speed;
  private int stealth;
  private int vision;
  private int pagesCollectedCount = 0;
  private int tries = 0;
  private int maxPagesCollectedCount = 0;

  public PlayerAttributeContainer(EventHub eventHub) {
    forget();

    eventHub.add(PageAcceptedEvent.class, event -> {
      pagesCollectedCount++;
      maxPagesCollectedCount = pagesCollectedCount;
      randomStatIncrease();
      eventHub.emit(new AttributesChangedEvent());
    });

    eventHub.add(ForgetEverythingEvent.class, event -> {
      forget();
      tries++;

      for (int i = 0; i < tries - 1; i++) {
        randomStatIncrease();
      }

      eventHub.emit(new AttributesChangedEvent());
    });
  }

  public int getSpeed() {
    return speed;
  }

  public int getStealth() {
    return stealth;
  }

  public int getVision() {
    return vision;
  }

  public int getTries() {
    return tries;
  }

  public int getPagesCollectedCount() {
    return pagesCollectedCount;
  }

  public int getMaxPagesCollectedCount() {
    return maxPagesCollectedCount;
  }

  private void forget() {
    speed = 3;
    stealth = 3;
    vision = 3;
    pagesCollectedCount = 0;
  }

  private void randomStatIncrease() {
    int speedChance = speed < 5 ? 33 : 0;
    int stealthChance = stealth < 5 ? 33 : 0;
    int visionChance = vision < 5 ? 33 : 0;
    int totalChance = speedChance + stealthChance + visionChance;

    if (totalChance == 0) {
      return;
    }

    int roll = RandomUtils.rollN(totalChance);

    if (roll < speedChance) {
      speed++;
    } else if (roll < speedChance + stealthChance) {
      stealth++;
    } else if (roll < speedChance + stealthChance + visionChance) {
      vision++;
    }
  }

  private void randomStatDecrease() {
    int speedChance = speed > 1 ? 33 : 0;
    int stealthChance = stealth > 1 ? 33 : 0;
    int visionChance = vision > 1 ? 33 : 0;
    int totalChance = speedChance + stealthChance + visionChance;

    if (totalChance == 0) {
      return;
    }

    int roll = RandomUtils.rollN(totalChance);

    if (roll < speedChance) {
      speed--;
    } else if (roll < speedChance + stealthChance) {
      stealth--;
    } else if (roll < speedChance + stealthChance + visionChance) {
      vision--;
    }
  }
}
