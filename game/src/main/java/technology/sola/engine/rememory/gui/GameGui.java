package technology.sola.engine.rememory.gui;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.attributes.PlayerAttributeContainer;

public class GameGui {
  public static GuiElement<?> build(SolaGuiDocument document, PlayerAttributeContainer playerAttributeContainer, EventHub eventHub) {
    eventHub.add(AttributesChangedEvent.class, event -> {
      document.getElementById("name", TextGuiElement.class).properties().setText(playerAttributeContainer.getName());
      updateAttributeValueText(document.getElementById("fitness", TextGuiElement.class), playerAttributeContainer.getFitness());
      updateAttributeValueText(document.getElementById("efficiency", TextGuiElement.class), playerAttributeContainer.getEfficiency());
      updateAttributeValueText(document.getElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
      updateAttributeValueText(document.getElementById("luck", TextGuiElement.class), playerAttributeContainer.getLuck());
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBackgroundColor(new Color(100, 255, 255, 255)),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(0),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Name:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setId("name")
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(0),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Fitness:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setId("fitness")
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(0),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Efficiency:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setId("efficiency")
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(0),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Vision:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setId("vision")
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(0),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Luck:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setId("luck")
        )
      )
    );
  }

  private static void updateAttributeValueText(TextGuiElement textGuiElement, int value) {
    textGuiElement.properties().setText("" + value);

    if (value < 5) {
      textGuiElement.properties().setColorText(Color.RED);
    } else if (value > 5) {
      textGuiElement.properties().setColorText(Color.GREEN);
    } else {
      textGuiElement.properties().setColorText(Color.BLACK);
    }
  }
}
