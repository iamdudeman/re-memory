package technology.sola.engine.rememory.attributes;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.events.ForgetWhoEvent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerAttributeContainer {
  private String name;
  private int fitness;
  private int efficiency;
  private int vision;
  private int luck;
  private final List<ReMemoryPage> acceptedPages = new ArrayList<>();

  public PlayerAttributeContainer(EventHub eventHub) {
    forget();

    eventHub.add(PageAcceptedEvent.class, event -> {
      acceptedPages.add(event.reMemoryPage());

      eventHub.emit(new AttributesChangedEvent());
    });

    eventHub.add(ForgetWhoEvent.class, event -> {
      forget();
      eventHub.emit(new AttributesChangedEvent());
    });
  }

  public List<ReMemoryPage> getAcceptedPages() {
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

  private void forget() {
    name = "???";
    fitness = 5;
    efficiency = 5;
    vision = 5;
    luck = 5;
    acceptedPages.clear();
  }
}
