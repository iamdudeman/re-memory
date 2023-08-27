package technology.sola.engine.rememory.attributes;

import java.util.List;
import java.util.Random;

public class ReMemoryMaker {
  private final PlayerAttributeContainer playerAttributeContainer;
  private final Random random = new Random();

  public ReMemoryMaker(PlayerAttributeContainer playerAttributeContainer) {
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public ReMemoryPage createPage() {
    AttributeCategory attributeCategory = determineCategory();
    AttributeModifier attributeModifier = determineModifier();

    List<String> currentNouns = playerAttributeContainer.getAcceptedPages().stream()
      .filter(page -> page.attributeCategory() == attributeCategory)
      .map(ReMemoryPage::noun)
      .toList();

    return new ReMemoryPage(attributeCategory, attributeModifier, NounDictionary.getRandomNoun(attributeCategory, currentNouns));
  }

  private AttributeModifier determineModifier() {
    int playerLuck = playerAttributeContainer.getLuck();
    int greatRange = playerLuck;
    int terribleRange = 10 - playerLuck;
    int goodRange = 45 + playerLuck * 2;

    int roll = random.nextInt(100);

    if (roll < greatRange) {
      return AttributeModifier.GREAT;
    } else if (roll < greatRange + terribleRange) {
      return AttributeModifier.TERRIBLE;
    } else if (roll < greatRange + terribleRange + goodRange) {
      return AttributeModifier.GOOD;
    }

    return AttributeModifier.BAD;
  }

  private AttributeCategory determineCategory() {
    int nameRange = 5;
    int jobRange = 15;
    int likeRange = 50;

    if (playerAttributeContainer.getAcceptedPages().stream().anyMatch(page -> page.attributeCategory() == AttributeCategory.NAME)) {
      nameRange = 0;
    }

    if (playerAttributeContainer.getAcceptedPages().stream().anyMatch(page -> page.attributeCategory() == AttributeCategory.JOB)) {
      jobRange = 0;
    }

    int roll = random.nextInt(100);

    if (roll < nameRange) {
      return AttributeCategory.NAME;
    } else if (roll < nameRange + jobRange) {
      return AttributeCategory.JOB;
    } else if (roll < nameRange + jobRange + likeRange) {
      return AttributeCategory.LIKES;
    } else {
      return AttributeCategory.INTERESTS;
    }
  }
}
