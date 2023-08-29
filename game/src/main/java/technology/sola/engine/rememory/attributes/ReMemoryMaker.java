package technology.sola.engine.rememory.attributes;

import technology.sola.engine.rememory.RandomUtils;

import java.util.List;

public class ReMemoryMaker {
  private final PlayerAttributeContainer playerAttributeContainer;

  public ReMemoryMaker(PlayerAttributeContainer playerAttributeContainer) {
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public ReMemoryPage createPage() {
    AttributeCategory attributeCategory = determineCategory();

    List<String> currentNouns = playerAttributeContainer.getAcceptedPages().stream()
      .filter(page -> page.attributeCategory() == attributeCategory)
      .map(ReMemoryPage::noun)
      .toList();

    return new ReMemoryPage(attributeCategory, NounDictionary.getRandomNoun(attributeCategory, currentNouns));
  }

  private AttributeCategory determineCategory() {
    int nameRange = playerAttributeContainer.getAcceptedPages().size() * 10;
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
