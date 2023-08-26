package technology.sola.engine.rememory.gui;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;

public class GameGui {
  public static GuiElement<?> build(SolaGuiDocument document) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBackgroundColor(new Color(100, 255, 255, 255)),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("Name: ???")
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(1),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Haste:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("4").setColorText(Color.RED)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(1),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Efficiency:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("6").setColorText(Color.GREEN)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(1),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Clarity:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("5").setColorText(Color.BLACK)
        )
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(1),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("Luck:")
        ),
        document.createElement(
          TextGuiElement::new,
          p -> p.setText("5").setColorText(Color.BLACK)
        )
      )
    );
  }
}
