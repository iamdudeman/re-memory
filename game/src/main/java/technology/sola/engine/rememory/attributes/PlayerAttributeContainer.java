package technology.sola.engine.rememory.attributes;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.events.ForgetWhereEvent;
import technology.sola.engine.rememory.events.ForgetEverythingEvent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerAttributeContainer {
  private int speed;
  private int stealth;
  private int vision;
  private final List<ReMemoryPage> acceptedPages = new ArrayList<>();
  private int pagesCollectedCount = 0;
  private int tries = 0;

  public PlayerAttributeContainer(EventHub eventHub) {
    forget();

    eventHub.add(PageAcceptedEvent.class, event -> {
      acceptedPages.add(event.reMemoryPage());
      pagesCollectedCount++;
      randomStatIncrease();
      eventHub.emit(new AttributesChangedEvent());
    });

    eventHub.add(ForgetWhereEvent.class, event -> {
      randomStatDecrease();
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

  public List<ReMemoryPage> getAcceptedPages() {
    return acceptedPages;
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

  private void forget() {
    speed = 3;
    stealth = 3;
    vision = 3;
    pagesCollectedCount = 0;
    acceptedPages.clear();
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
