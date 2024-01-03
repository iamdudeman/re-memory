package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiDocument;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class GuiManager {
  private final SolaEcs solaEcs;
  private final EventHub eventHub;
  private final PlayerAttributeContainer playerAttributeContainer;

  private final ConditionalStyle<TextStyles> negativeTextStyle = ConditionalStyle.always(
    TextStyles.create()
      .setTextColor(new Color(230, 159, 0))
      .build()
  );
  private final ConditionalStyle<TextStyles> positiveTextStyle = ConditionalStyle.always(
    TextStyles.create()
      .setTextColor(new Color(0, 114, 178))
      .build()
  );
  private final ConditionalStyle<TextStyles> visibilityVisibleStyle = ConditionalStyle.always(
    TextStyles.create()
      .setVisibility(Visibility.VISIBLE)
      .build()
  );

  private boolean showingPlayerMessage = false;

  public GuiManager(SolaEcs solaEcs, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer) {
    this.solaEcs = solaEcs;
    this.eventHub = eventHub;
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public void initialize(GuiDocument guiDocument, GuiJsonDocument controlsDocument, GuiJsonDocument inGameDocument, GuiJsonDocument diaryDocument) {
    guiDocument.setRootElement(controlsDocument.rootElement());

    // controls
    controlsDocument.rootElement().events().keyPressed().on(guiKeyEvent -> {
      if (guiKeyEvent.getKeyEvent().keyCode() == Key.SPACE.getCode() || guiKeyEvent.getKeyEvent().keyCode() == Key.RIGHT.getCode()) {
        setGamePause(false);
        guiDocument.setRootElement(inGameDocument.rootElement());
      }
    });

    controlsDocument.rootElement().events().mousePressed().on(guiKeyEvent -> {
      setGamePause(false);
      guiDocument.setRootElement(inGameDocument.rootElement());
    });

    // in_game
    eventHub.add(AttributesChangedEvent.class, event -> {
      updateAttributeValueText(inGameDocument.rootElement().findElementById("speed", TextGuiElement.class), playerAttributeContainer.getSpeed());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("stealth", TextGuiElement.class), playerAttributeContainer.getStealth());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("pages", TextGuiElement.class), playerAttributeContainer.getPagesCollectedCount());
    });

    var attributesSectionElement = inGameDocument.rootElement().findElementById("attributes", SectionGuiElement.class);
    var pageTextElement = inGameDocument.rootElement().findElementById("page", TextGuiElement.class);
    var pageStyles = pageTextElement.getStyles();

    pageTextElement.events().mousePressed().on(guiMouseEvent -> {
      if (!showingPlayerMessage) {
        return;
      }

      if (playerAttributeContainer.getPagesCollectedCount() < 4) {
        pageStyles.removeStyle(visibilityVisibleStyle);
        pageStyles.invalidate();

        eventHub.emit(new PageAcceptedEvent());

        setGamePause(false);
      } else {
        eventHub.emit(new PageAcceptedEvent());

        guiDocument.setRootElement(diaryDocument.rootElement());

        solaEcs.getSystems().forEach(system -> system.setActive(false));
        solaEcs.setWorld(new World(1));
      }
    });

    attributesSectionElement.events().keyPressed().on(guiKeyEvent -> {
      if (!showingPlayerMessage) {
        return;
      }

      if (guiKeyEvent.getKeyEvent().keyCode() == Key.SPACE.getCode() || guiKeyEvent.getKeyEvent().keyCode() == Key.RIGHT.getCode()) {
        if (playerAttributeContainer.getPagesCollectedCount() < 4) {
          pageStyles.removeStyle(visibilityVisibleStyle);
          pageStyles.invalidate();

          eventHub.emit(new PageAcceptedEvent());

          setGamePause(false);
        } else {
          eventHub.emit(new PageAcceptedEvent());

          guiDocument.setRootElement(diaryDocument.rootElement());

          solaEcs.getSystems().forEach(system -> system.setActive(false));
          solaEcs.setWorld(new World(1));
        }
      }

      guiKeyEvent.stopPropagation();
    });

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(PageComponent.class),
        (player, page) -> {
          page.destroy();

          setGamePause(true);

          pageTextElement.setText(getNextText(playerAttributeContainer));
          pageStyles.addStyle(visibilityVisibleStyle);
          pageStyles.invalidate();

          attributesSectionElement.requestFocus();
          showingPlayerMessage = true;
        }
      );
    });
  }

  private void setGamePause(boolean isPaused) {
    solaEcs.getSystem(EnemySystem.class).setActive(!isPaused);
    solaEcs.getSystem(PlayerSystem.class).setActive(!isPaused);
    solaEcs.getSystem(PhysicsSystem.class).setActive(!isPaused);
  }

  private void updateAttributeValueText(TextGuiElement textGuiElement, int value) {
    textGuiElement.setText("" + value);

    textGuiElement.getStyles().removeStyle(negativeTextStyle);
    textGuiElement.getStyles().removeStyle(positiveTextStyle);

    if (value < 3) {
      textGuiElement.getStyles().addStyle(negativeTextStyle);
    } else if (value > 3) {
      textGuiElement.getStyles().addStyle(positiveTextStyle);
    }

    textGuiElement.getStyles().invalidate();
  }

  private static String getNextText(PlayerAttributeContainer playerAttributeContainer) {
    int pagesCollected = playerAttributeContainer.getPagesCollectedCount();
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
