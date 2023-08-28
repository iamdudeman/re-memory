package technology.sola.engine.rememory.gui;

import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.properties.Display;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.attributes.PlayerAttributeContainer;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

public class GameGui {
  public static GuiElement<?> build(SolaGuiDocument document, PlayerAttributeContainer playerAttributeContainer, EventHub eventHub) {
    eventHub.add(AttributesChangedEvent.class, event -> {
      document.getElementById("name", TextGuiElement.class).properties().setText(playerAttributeContainer.getName());
      updateAttributeValueText(document.getElementById("fitness", TextGuiElement.class), playerAttributeContainer.getFitness());
      updateAttributeValueText(document.getElementById("stealth", TextGuiElement.class), playerAttributeContainer.getStealth());
      updateAttributeValueText(document.getElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
      updateAttributeValueText(document.getElementById("luck", TextGuiElement.class), playerAttributeContainer.getLuck());
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(2).setDirection(StreamGuiElementContainer.Direction.HORIZONTAL).setBackgroundColor(new Color(100, 255, 255, 255)).padding.set(1),
      createAttributeContainer(document),
      createReadPageContainer(document, eventHub)
    );
  }

  private static GuiElement<?> createReadPageContainer(SolaGuiDocument document, EventHub eventHub) {
    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(PageComponent.class),
        (player, page) -> {
          var pageComponent = page.getComponent(PageComponent.class);
          var pageContainer = document.getElementById("page_container").properties();

          page.destroy();

          document.getElementById("page", TextGuiElement.class).properties().setText(pageComponent.reMemoryPage().getPageText());
          document.getElementById("remember", ButtonGuiElement.class).setOnAction(() -> {
            pageContainer.setDisplay(Display.NONE);
            eventHub.emit(new PageAcceptedEvent(pageComponent.reMemoryPage()));
          });
          document.getElementById("deny", ButtonGuiElement.class).setOnAction(() -> {
            pageContainer.setDisplay(Display.NONE);
          });
          pageContainer.setDisplay(Display.DEFAULT);
        }
      );
    });

    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(3).setDirection(StreamGuiElementContainer.Direction.VERTICAL).setId("page_container").setDisplay(Display.NONE),
      document.createElement(
        TextGuiElement::new,
        p -> p.setWidth(165).padding.set(2, 5).setBorderColor(Color.BLACK).setId("page")
      ),
      document.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(3),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Deny").padding.set(2).setBackgroundColor(Color.WHITE).setId("deny").setFocusable(false).hover.setBackgroundColor(Color.WHITE.shade(0.1f))
        ),
        document.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Remember").padding.set(2).setBackgroundColor(Color.WHITE).setId("remember").setFocusable(false).hover.setBackgroundColor(Color.WHITE.shade(0.1f))
        )
      )
    );
  }

  private static GuiElement<?> createAttributeContainer(SolaGuiDocument document) {
    return document.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBorderColor(Color.DARK_GRAY).padding.set(2),
      createAttributeText(document, "Name:", "name"),
      createAttributeText(document, "Fitness:", "fitness"),
      createAttributeText(document, "Stealth:", "stealth"),
      createAttributeText(document, "Vision:", "vision"),
      createAttributeText(document, "Luck:", "luck")
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

    if (value < 5) {
      textGuiElement.properties().setColorText(new Color(230, 159, 0));
    } else if (value > 5) {
      textGuiElement.properties().setColorText(new Color(0, 114, 178));
    } else {
      textGuiElement.properties().setColorText(Color.BLACK);
    }
  }
}
