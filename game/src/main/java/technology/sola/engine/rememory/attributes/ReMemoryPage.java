package technology.sola.engine.rememory.attributes;

public class ReMemoryPage {
  private AttributeCategory attributeCategory;
  private AttributeModifier attributeModifier;
  private String text = "This is some fake text";

  public AttributeCategory getAttributeCategory() {
    return attributeCategory;
  }

  public AttributeModifier getAttributeModifier() {
    return attributeModifier;
  }

  public String noun;

  public String getPageText() {
    return text;
  }
}
