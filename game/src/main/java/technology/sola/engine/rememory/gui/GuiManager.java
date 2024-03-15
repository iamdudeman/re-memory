package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Visibility;
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
  private final String[] pages = {
    //    "  I am writing this to you, the future 'me'. ",

    "  I don't know what's been going on recently. I've been feeling so confused about what I'm doing and forgetful of things I need to do or have been doing. Do I need more sleep? Do I need to change my diet? Is this just what getting older is like? Until I figure out what's happening, I'm going to start writing things down in this journal to make sure I remember the things I need to.",
    "  This is worse than I could have possibly imagined. Just got off the phone with my doctor. He gave me the grim diagnosis. Alzheimer's. I should have known. The forgetfulness and confusion have only gotten worse. It feels like I get lost in my own home sometimes. Sometimes it takes a while to remember the names of my own children. I feel so scared and alone.",
    "  Today I visited the old lighthouse my grandpa operated when I was a kid. It's the first time in a long time that I've felt any peace. Memories came flooding back. Memories of sitting on his lap as he'd tell me stories or sing old hymns. Like a light shining in darkness, this is the only place I can go to see clearly anymore.",
    "  It's gotten so much worse. I can't think. I can't follow conversations. I can't remember my own children. I need people to guide me around my own home. The only time I can do anything is when I'm here at the lighthouse. It feels like my own brain wants to destroy me. I can't do anything right anymore because I've forgotten how. My loved ones are being taken from me by my own mind. I feel so hopeless. I'm so alone. God help me...",
    "  I experience the memories of a stranger every day. Each day the same process of \"re-memory\" as I struggle to find and read through this diary. But even so, even if the \"me\" tomorrow is once again a stranger; even if the rest of my days are just re-memories of the past; I will trust in the Light that pierces even this growing darkness.                                                                                           John 1:5",
    "~~~~~Thank you for playing~~~~~\nMusic-@Eliteomnomnivore Code-@iamdudeman\nArt-@Denise\nArt-@iamdudeman\nArt-@Eliteomnomnivore\nStory-@Lvl30Caterpie\nStory-@iamdudeman",
  };
  private int pageIndex = 0;

  private boolean showingPlayerMessage = false;

  public GuiManager(SolaEcs solaEcs, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer) {
    this.solaEcs = solaEcs;
    this.eventHub = eventHub;
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public void initialize(GuiDocument guiDocument, GuiJsonDocument controlsDocument, GuiJsonDocument inGameDocument, GuiJsonDocument diaryDocument) {
    guiDocument.setRootElement(controlsDocument.rootElement());

    initializeControlsGui(guiDocument, controlsDocument, inGameDocument);

    initializeInGameGui(guiDocument, inGameDocument, diaryDocument);

    initializeDiaryGui(guiDocument, diaryDocument, inGameDocument);
  }

  private void initializeControlsGui(GuiDocument guiDocument, GuiJsonDocument controlsDocument, GuiJsonDocument inGameDocument) {
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
  }

  private void initializeInGameGui(GuiDocument guiDocument, GuiJsonDocument inGameDocument, GuiJsonDocument diaryDocument) {
    eventHub.add(AttributesChangedEvent.class, event -> {
      updateAttributeValueText(inGameDocument.rootElement().findElementById("speed", TextGuiElement.class), playerAttributeContainer.getSpeed());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("stealth", TextGuiElement.class), playerAttributeContainer.getStealth());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("pages", TextGuiElement.class), playerAttributeContainer.getPagesCollectedCount());
    });

    var attributesSectionElement = inGameDocument.rootElement().findElementById("attributes", SectionGuiElement.class);
    var pageTextElement = inGameDocument.rootElement().findElementById("page", TextGuiElement.class);
    var pageStyles = pageTextElement.styles();

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

          pageTextElement.setText(getNextEnemyResponseText(playerAttributeContainer));
          pageStyles.addStyle(visibilityVisibleStyle);
          pageStyles.invalidate();

          attributesSectionElement.requestFocus();
          showingPlayerMessage = true;
        }
      );
    });
  }

  private void initializeDiaryGui(GuiDocument guiDocument, GuiJsonDocument diaryDocument, GuiJsonDocument inGameDocument) {
    var diaryTextElement = diaryDocument.rootElement().findElementById("diary", TextGuiElement.class);
    var diaryContainerElement = diaryDocument.rootElement().findElementById("diary-container", SectionGuiElement.class);

    diaryTextElement.setText(pages[pageIndex]);

    final Runnable changePage = () -> {
      if (pageIndex > pages.length - 1) {
        pageIndex = pages.length - 1;
      } else if (pageIndex < 0) {
        pageIndex = 0;
      }

      diaryTextElement.setText(pages[pageIndex]);
    };

    diaryContainerElement.events().keyPressed().on(guiKeyEvent -> {
      if (guiKeyEvent.getKeyEvent().keyCode() == Key.RIGHT.getCode() || guiKeyEvent.getKeyEvent().keyCode() == Key.SPACE.getCode()) {
        pageIndex++;
        changePage.run();
      } else if (guiKeyEvent.getKeyEvent().keyCode() == Key.LEFT.getCode()) {
        pageIndex--;
        changePage.run();
      }
    });
    diaryContainerElement.events().mousePressed().on(guiMouseEvent -> {
      pageIndex++;
      changePage.run();
    });
  }

  private void setGamePause(boolean isPaused) {
    solaEcs.getSystem(EnemySystem.class).setActive(!isPaused);
    solaEcs.getSystem(PlayerSystem.class).setActive(!isPaused);
    solaEcs.getSystem(PhysicsSystem.class).setActive(!isPaused);
  }

  private void updateAttributeValueText(TextGuiElement textGuiElement, int value) {
    textGuiElement.setText("" + value);

    textGuiElement.styles().removeStyle(negativeTextStyle);
    textGuiElement.styles().removeStyle(positiveTextStyle);

    if (value < 3) {
      textGuiElement.styles().addStyle(negativeTextStyle);
    } else if (value > 3) {
      textGuiElement.styles().addStyle(positiveTextStyle);
    }

    textGuiElement.styles().invalidate();
  }

  private static String getNextEnemyResponseText(PlayerAttributeContainer playerAttributeContainer) {
    int pagesCollected = playerAttributeContainer.getPagesCollectedCount();
    int tries = playerAttributeContainer.getTries();

    return switch (pagesCollected) {
      case 0 -> {
        if (tries == 1) {
          yield "Hey, props to you for trying to remember, but it only gets harder from here.";
        } else if (tries == 2) {
          yield "See, I told you didn't I? Finding four more pages isn't worth the effort!";
        } else {
          yield "You're not making any progress. You've earned your rest already just give it up.";
        }
      }
      case 1 -> {
        if (tries == 1) {
          yield "I wasn't lying to you ya know. It's gonna get rougher...";
        } else if (tries == 2) {
          yield "Congrats on another step forward. We should go back to the forest and celebrate!";
        } else {
          yield "You haven't even made it halfway yet. Why do you keep trying to remember this?";
        }
      }
      case 2 -> {
        if (tries == 1) {
          yield "Ahh you already found the lighthouse. We can't have you getting ahead of yourself now.";
        } else if (tries == 2) {
          yield "I've always been with you. Seeing this lighthouse again changes nothing.";
        } else {
          yield "Took you long enough to get back here. Was this lighthouse really worth it?";
        }
      }
      case 3 -> {
        if (tries == 1) {
          yield "Remembering will just bring you pain. Stay with me instead!";
        } else if (tries == 2) {
          yield "Would you leave me behind after all that I have done for you?";
        } else {
          yield "After so many failures you've done well to make it this far. Let's go back and rest now for awhile.";
        }
      }
      case 4 -> "You don't need to remember! It's not to late to turn back...";
      default -> "If you're reading this, then the game has a bug!";
    };
  }
}
