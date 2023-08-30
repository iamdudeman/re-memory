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
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.attributes.PlayerAttributeContainer;
import technology.sola.engine.rememory.events.PageAcceptedEvent;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class GameGui {
  public static GuiElement<?> build(SolaGuiDocument document, PlayerAttributeContainer playerAttributeContainer, EventHub eventHub, SolaEcs solaEcs) {
    eventHub.add(AttributesChangedEvent.class, event -> {
      updateAttributeValueText(document.getElementById("speed", TextGuiElement.class), playerAttributeContainer.getSpeed());
      updateAttributeValueText(document.getElementById("stealth", TextGuiElement.class), playerAttributeContainer.getStealth());
      updateAttributeValueText(document.getElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(2).setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBackgroundColor(new Color(100, 255, 255, 255)).padding.set(2),
      createAttributeContainer(document),
      createReadPageContainer(document, eventHub, solaEcs)
    );
  }

  private static GuiElement<?> createReadPageContainer(SolaGuiDocument document, EventHub eventHub, SolaEcs solaEcs) {
    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(PageComponent.class),
        (player, page) -> {
          var pageComponent = page.getComponent(PageComponent.class);
          var pageContainer = document.getElementById("page_container");
          var rememberButton = document.getElementById("remember", ButtonGuiElement.class);

          page.destroy();

          setGamePause(solaEcs, true);

          document.getElementById("page", TextGuiElement.class).properties().setText(pageComponent.reMemoryPage().getPageText());
          rememberButton.setOnAction(() -> {
            pageContainer.properties().setDisplay(Display.NONE);
            eventHub.emit(new PageAcceptedEvent(pageComponent.reMemoryPage()));

            setGamePause(solaEcs, false);
          });
          pageContainer.properties().setDisplay(Display.DEFAULT);
          rememberButton.requestFocus();
        }
      );
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(3).setDirection(StreamGuiElementContainer.Direction.VERTICAL).setId("page_container").setDisplay(Display.NONE),
      document.createElement(
        TextGuiElement::new,
        p -> p.setWidth(200).padding.set(2, 5).setBorderColor(Color.BLACK).setId("page")
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(3),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Remember").padding.set(2).setBackgroundColor(Color.WHITE).setId("remember").hover.setBackgroundColor(Color.WHITE.shade(0.1f))
        )
      )
    );
  }

  private static void setGamePause(SolaEcs solaEcs, boolean isPaused) {
    solaEcs.getSystem(EnemySystem.class).setActive(!isPaused);
    solaEcs.getSystem(PlayerSystem.class).setActive(!isPaused);
    solaEcs.getSystem(PhysicsSystem.class).setActive(!isPaused);
  }

  private static GuiElement<?> createAttributeContainer(SolaGuiDocument document) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(10).setDirection(StreamGuiElementContainer.Direction.HORIZONTAL).setBorderColor(Color.DARK_GRAY).padding.set(1).setWidth(200),
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
