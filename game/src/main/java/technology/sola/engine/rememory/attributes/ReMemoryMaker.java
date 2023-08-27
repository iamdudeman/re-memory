package technology.sola.engine.rememory.attributes;

import java.util.Random;

public class ReMemoryMaker {
  private final PlayerAttributeContainer playerAttributeContainer;
  private final Random random = new Random();

  public ReMemoryMaker(PlayerAttributeContainer playerAttributeContainer) {
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public ReMemoryPage createPage() {
    int playerLuck = playerAttributeContainer.getLuck();

    AttributeCategory attributeCategory = AttributeCategory.NAME;
    AttributeModifier attributeModifier = AttributeModifier.GOOD;

    return new ReMemoryPage();
  }
}
