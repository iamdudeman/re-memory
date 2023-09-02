package technology.sola.engine.rememory.gui;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.input.Key;

import java.util.function.Consumer;

public class ControlsGui {
  static GuiElement<?> build(Consumer<Integer> changeGui, Consumer<Boolean> gamePause, SolaGuiDocument document) {
    var controlsContainer = document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(3).setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBackgroundColor(Color.WHITE).padding.set(5),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("Controls").margin.setBottom(5)
      ),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("WASD - Move around")
      ),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("F - Reset portal destinations")
      ),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("Space/Click - Cycle GUI text")
      ),
      document.createElement(
        TextGuiElement::new,
        p -> p.setText("Left/Right arrows - Cycle GUI text")
      )
    );

    controlsContainer.setOnKeyPressCallback(keyEvent -> {
      if (keyEvent.getKeyCode() == Key.SPACE.getCode() || keyEvent.getKeyCode() == Key.RIGHT.getCode()) {
        changeGui.accept(1);
        gamePause.accept(false);
      }
    });
    controlsContainer.setOnMouseDownCallback(mouseEvent -> {
      changeGui.accept(1);
      gamePause.accept(false);
    });

    return controlsContainer;
  }
}
