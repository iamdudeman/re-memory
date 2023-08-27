package technology.sola.engine.rememory.attributes;

public record ReMemoryPage(AttributeCategory attributeCategory, AttributeModifier attributeModifier, String noun) {
  public String getPageText() {
    return switch (attributeCategory) {
      case NAME -> "Your name was " + noun;
      case JOB -> "Your profession was " + noun;
      case LIKES -> "You potentially liked " + noun;
      case INTERESTS -> "You possibly had interest in " + noun;
    };
  }
}
