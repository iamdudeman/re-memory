package technology.sola.engine.rememory.gui;

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
            if (keyEvent.getKeyCode() == Key.SPACE.getCode() || keyEvent.getKeyCode() == Key.RIGHT.getCode()) {
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

  // TODO finalize real text
  private static String getNextText(PlayerAttributeContainer playerAttributeContainer) {
    int pagesCollected = playerAttributeContainer.getPagesCollectedCount();
    int maxPagesCollected = playerAttributeContainer.getMaxPagesCollectedCount();
    int tries = playerAttributeContainer.getTries();

    return switch (pagesCollected) {
      case 0 -> {
        if (tries == 1) {
          yield "Ah, I remember writing these. I need to find them. Four more, I think.";
        } else if (tries == 2) {
          yield "I need to keep trying. I can do it this time. Four more to go.";
        } else {
          yield "Here we go again.";
        }
      }
      case 1 -> {
        if (tries == 1) {
          yield "Here's another one! I need to find three more.";
        } else if (tries == 2) {
          yield "I can do this. Three more to go.";
        } else {
          yield "I need to focus. Three more to find.";
        }
      }
      case 2 -> {
        if (tries == 1) {
          yield "I think I'm starting to remember. Two more to go.";
        } else if (tries == 2) {
          yield "Things are starting to clear up again. Two more to find.";
        } else {
          yield "Gotta keep trying until I remember. Only two left to find.";
        }
      }
      case 3 -> {
        if (tries == 1) {
          yield "Everything is getting clear. Only one more page to go.";
        } else if (tries == 2) {
          yield "I'm so close, I can't give up now. Only one page left.";
        } else {
          yield "One more push and then I've got them all.";
        }
      }
      case 4 -> "I've done it. I remember now...";
      default -> "If you're reading this, then the game has a bug!";
    };
  }
}
