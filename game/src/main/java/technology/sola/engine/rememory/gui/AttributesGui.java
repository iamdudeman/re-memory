package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.events.AttributesChangedEvent;

class AttributesGui {
  static GuiElement<?> build(SolaGuiDocument document, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer, SolaEcs solaEcs) {
    eventHub.add(AttributesChangedEvent.class, event -> {
      if (document.getElementById("speed") == null) {
        return;
      }

      updateAttributeValueText(document.getElementById("speed", TextGuiElement.class), playerAttributeContainer.getSpeed());
      updateAttributeValueText(document.getElementById("stealth", TextGuiElement.class), playerAttributeContainer.getStealth());
      updateAttributeValueText(document.getElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(2).setDirection(StreamGuiElementContainer.Direction.VERTICAL).padding.set(1).setBackgroundColor(Color.BLANK),
      createAttributeContainer(document)
    );
  }

  private static GuiElement<?> createAttributeContainer(SolaGuiDocument document) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(10).setDirection(StreamGuiElementContainer.Direction.HORIZONTAL).setBorderColor(Color.DARK_GRAY).setBackgroundColor(new Color(100, 255, 255, 255)).padding.set(1).setWidth(200),
      createAttributeText(document, "Speed:", "speed"),
      createAttributeText(document, "Stealth:", "stealth"),
      createAttributeText(document, "Vision:", "vision")
    );
  }

  private static GuiElement<?> createAttributeText(SolaGuiDocument document, String label, String id) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(0),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText(label)
      ),
      document.createElement(
        TextGuiElement::new,
        p -> p.setId(id)
      )
    );
  }

  private static void updateAttributeValueText(TextGuiElement textGuiElement, int value) {
    textGuiElement.properties().setText("" + value);

    if (value < 3) {
      textGuiElement.properties().setColorText(new Color(230, 159, 0));
    } else if (value > 3) {
      textGuiElement.properties().setColorText(new Color(0, 114, 178));
    } else {
      textGuiElement.properties().setColorText(Color.BLACK);
    }
  }
}
