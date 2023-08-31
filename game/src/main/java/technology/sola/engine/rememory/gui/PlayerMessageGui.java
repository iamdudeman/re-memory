package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.properties.Display;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

import java.util.function.Consumer;

class PlayerMessageGui {
  static GuiElement<?> build(Consumer<Integer> changeGui, SolaGuiDocument document, EventHub eventHub, SolaEcs solaEcs) {
    var textElement = document.createElement(
      TextGuiElement::new,
      p -> p.setWidth(254).setHeight(56).padding.set(2, 5).setId("page").setBackgroundColor(Color.WHITE).setBorderColor(Color.BLACK)
    );

    textElement.properties().setFocusable(true);

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(PageComponent.class),
        (player, page) -> {
          var pageComponent = page.getComponent(PageComponent.class);

          page.destroy();

          setGamePause(solaEcs, true);

          textElement.properties().setText(pageComponent.reMemoryPage().getPageText());
          changeGui.accept(2);

          textElement.setOnMouseDownCallback(mouseEvent -> {
            changeGui.accept(1);

            eventHub.emit(new PageAcceptedEvent(pageComponent.reMemoryPage()));

            setGamePause(solaEcs, false);
          });
          textElement.setOnKeyPressCallback(keyEvent -> {
            if (keyEvent.getKeyCode() == Key.ENTER.getCode() || keyEvent.getKeyCode() == Key.SPACE.getCode()) {
              changeGui.accept(1);

              eventHub.emit(new PageAcceptedEvent(pageComponent.reMemoryPage()));

              setGamePause(solaEcs, false);
            }
          });
          textElement.requestFocus();
        }
      );
    });

    return textElement;
  }

  private static void setGamePause(SolaEcs solaEcs, boolean isPaused) {
    solaEcs.getSystem(EnemySystem.class).setActive(!isPaused);
    solaEcs.getSystem(PlayerSystem.class).setActive(!isPaused);
    solaEcs.getSystem(PhysicsSystem.class).setActive(!isPaused);
  }
}
