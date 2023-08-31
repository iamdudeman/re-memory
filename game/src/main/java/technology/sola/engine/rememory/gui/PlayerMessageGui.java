package technology.sola.engine.rememory.gui;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

import java.util.function.Consumer;

class PlayerMessageGui {
  static GuiElement<?> build(Consumer<Integer> changeGui, SolaGuiDocument document, EventHub eventHub, SolaEcs solaEcs, PlayerAttributeContainer playerAttributeContainer) {
    var textElement = document.createElement(
      TextGuiElement::new,
      p -> p.setWidth(253).setHeight(48).padding.set(2, 4).setId("page").setBackgroundColor(Color.WHITE).setBorderColor(Color.BLACK)
    );

    textElement.properties().setFocusable(true);

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(PageComponent.class),
        (player, page) -> {
          page.destroy();

          setGamePause(solaEcs, true);

          textElement.properties().setText(getNextText(playerAttributeContainer));
          changeGui.accept(2);

          textElement.setOnMouseDownCallback(mouseEvent -> {
            if (playerAttributeContainer.getPagesCollectedCount() < 4) {
              changeGui.accept(1);

              eventHub.emit(new PageAcceptedEvent());

              setGamePause(solaEcs, false);
            } else {
              eventHub.emit(new PageAcceptedEvent());

              changeGui.accept(3);

              solaEcs.getSystems().forEach(system -> system.setActive(false));
              solaEcs.setWorld(new World(1));
            }
          });
          textElement.setOnKeyPressCallback(keyEvent -> {
            if (keyEvent.getKeyCode() == Key.ENTER.getCode() || keyEvent.getKeyCode() == Key.SPACE.getCode()) {
              changeGui.accept(1);

              eventHub.emit(new PageAcceptedEvent());

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

  // TODO real text
  private static String getNextText(PlayerAttributeContainer playerAttributeContainer) {
    int pagesCollected = playerAttributeContainer.getPagesCollectedCount();
    int maxPagesCollected = playerAttributeContainer.getMaxPagesCollectedCount();
    int tries = playerAttributeContainer.getTries();

    return switch (pagesCollected) {
      case 0 -> {
        if (maxPagesCollected > 1) {
          yield "Just four more";
        } else {
          yield "What were these for again? More text to wrap to next line. Even more text to wrap to next line. Doot doot doot.";
        }
      }
      case 1 -> "I think I need 3 more";
      case 2 -> "Just 2 more";
      case 3 -> "1 more now";
      case 4 -> "That should be all of them";
      default -> "Ran out of PoC text";
    };
  }
}
