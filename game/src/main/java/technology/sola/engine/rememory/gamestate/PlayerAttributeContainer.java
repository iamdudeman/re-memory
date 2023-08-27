package technology.sola.engine.rememory.gamestate;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.events.ForgetWhoEvent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerAttributeContainer {
  private String name;
  private int fitness;
  private int efficiency;
  private int vision;
  private int luck;
  private final Map<AttributeCategory, ReMemoryPage> acceptedPages = new HashMap<>();

  public PlayerAttributeContainer(EventHub eventHub) {
    forget();

    eventHub.add(PageAcceptedEvent.class, event -> {
      // todo

      eventHub.emit(new AttributesChangedEvent());
    });

    eventHub.add(ForgetWhoEvent.class, event -> {
      forget();
      eventHub.emit(new AttributesChangedEvent());
    });
  }

  private void forget() {
    // todo emit change event
    name = "???";
    fitness = 5;
    efficiency = 5;
    vision = 5;
    luck = 5;
    acceptedPages.clear();
  }

  public Map<AttributeCategory, ReMemoryPage> getAcceptedPages() {
    return acceptedPages;
  }

  public String getName() {
    return name;
  }

  public int getFitness() {
    return fitness;
  }

  public int getEfficiency() {
    return efficiency;
  }

  public int getVision() {
    return vision;
  }

  public int getLuck() {
    return luck;
  }
}
