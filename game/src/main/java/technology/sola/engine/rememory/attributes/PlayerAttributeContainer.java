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
      applyPageAttributes(event.reMemoryPage());
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

  private void applyPageAttributes(ReMemoryPage reMemoryPage) {
    if (reMemoryPage.attributeCategory() == AttributeCategory.NAME) {
      name = reMemoryPage.noun();
      fitness++;
      efficiency++;
      vision++;
      luck++;
    } else {
      // todo not just fitness
      fitness += updateAttribute(fitness, reMemoryPage.attributeModifier());
    }
  }

  private int updateAttribute(int value, AttributeModifier attributeModifier) {
    int newValue = value;

    newValue += switch (attributeModifier) {
      case GREAT -> 2;
      case GOOD -> 1;
      case BAD -> -1;
      case TERRIBLE -> -2;
    };

    return clampAttribute(newValue);
  }

  private int clampAttribute(int value) {
    if (value < 1) {
      return 1;
    }

    return Math.min(value, 10);
  }
}
