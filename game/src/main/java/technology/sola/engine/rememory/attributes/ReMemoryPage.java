package technology.sola.engine.rememory.attributes;

public class ReMemoryPage {
  private AttributeType attributeType;
  private AttributeCategory attributeCategory;
  private AttributeModifier attributeModifier;
  private String text = "This is some fake text";

  public AttributeType getAttributeType() {
    return attributeType;
  }

  public AttributeCategory getAttributeCategory() {
    return attributeCategory;
  }

  public AttributeModifier getAttributeModifier() {
    return attributeModifier;
  }

  public String getText() {
    return text;
  }
}
