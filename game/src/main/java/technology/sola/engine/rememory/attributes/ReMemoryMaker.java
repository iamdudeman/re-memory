package technology.sola.engine.rememory.attributes;

import technology.sola.engine.rememory.RandomUtils;

import java.util.List;

public class ReMemoryMaker {
  private final PlayerAttributeContainer playerAttributeContainer;

  public ReMemoryMaker(PlayerAttributeContainer playerAttributeContainer) {
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public ReMemoryPage createPage() {
    Attribute attribute = determineAttribute();
    AttributeCategory attributeCategory = determineCategory();
    AttributeModifier attributeModifier = determineModifier();

    List<String> currentNouns = playerAttributeContainer.getAcceptedPages().stream()
      .filter(page -> page.attributeCategory() == attributeCategory)
      .map(ReMemoryPage::noun)
      .toList();

    return new ReMemoryPage(attributeCategory, attributeModifier, attribute, NounDictionary.getRandomNoun(attributeCategory, currentNouns));
  }

  private Attribute determineAttribute() {
    int fitnessRange = 33;
    int visionRange = 33;
    int luckRange = 33;

    int roll = RandomUtils.roll100();

    if (roll < fitnessRange) {
      return Attribute.FITNESS;
    } else if (roll < fitnessRange + visionRange) {
      return Attribute.VISION;
    } else if (roll < fitnessRange + visionRange + luckRange) {
      return Attribute.LUCK;
    }

    return Attribute.STEALTH;
  }

  private AttributeModifier determineModifier() {
    int playerLuck = playerAttributeContainer.getLuck();
    int greatRange = playerLuck;
    int terribleRange = 10 - playerLuck;
    int goodRange = 45 + playerLuck * 2;

    int roll = RandomUtils.roll100();

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
    int nameRange = playerAttributeContainer.getLuck() + playerAttributeContainer.getAcceptedPages().size() * 10;
    int jobRange = 15 + playerAttributeContainer.getAcceptedPages().size() * 10;
    int likeRange = 45;

    if (playerAttributeContainer.getAcceptedPages().stream().anyMatch(page -> page.attributeCategory() == AttributeCategory.NAME)) {
      nameRange = 0;
    }

    if (playerAttributeContainer.getAcceptedPages().stream().anyMatch(page -> page.attributeCategory() == AttributeCategory.JOB)) {
      jobRange = 0;
    }

    int roll = RandomUtils.roll100();

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
