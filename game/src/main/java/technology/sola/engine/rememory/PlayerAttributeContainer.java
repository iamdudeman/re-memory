package technology.sola.engine.rememory;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.events.ForgetPagesEvent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;
import technology.sola.engine.rememory.events.StatIncreaseEvent;

public class PlayerAttributeContainer {
  public static final int STAT_CAP = 9;
  private int speed = 1;
  private int stealth = 1;
  private int vision = 1;
  private int pagesCollectedCount = 0;
  private int tries = 0;
  private int maxPagesCollectedCount = 0;

  public PlayerAttributeContainer(EventHub eventHub) {
    forget();

    eventHub.add(PageAcceptedEvent.class, event -> {
      pagesCollectedCount++;

      if (pagesCollectedCount > maxPagesCollectedCount) {
        maxPagesCollectedCount = pagesCollectedCount;
      }

      eventHub.emit(new AttributesChangedEvent());
    });

    eventHub.add(StatIncreaseEvent.class, event -> {
      randomStatIncrease();
      eventHub.emit(new AttributesChangedEvent());
    });

    eventHub.add(ForgetPagesEvent.class, event -> {
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

  public int getStatCount() {
    return speed + stealth + vision;
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
    pagesCollectedCount = 0;
  }

  private void randomStatIncrease() {
    int speedChance = speed < STAT_CAP ? 33 : 0;
    int stealthChance = stealth < STAT_CAP ? 33 : 0;
    int visionChance = vision < STAT_CAP ? 33 : 0;
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
}
