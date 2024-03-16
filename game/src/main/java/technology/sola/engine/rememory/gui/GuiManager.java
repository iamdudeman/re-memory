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
  private int pageIndex = 0;
  private boolean isFullDiary = false;

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

      if (playerAttributeContainer.getPagesCollectedCount() < DialogUtil.pages.length - 2) {
        pageStyles.removeStyle(visibilityVisibleStyle);
        pageStyles.invalidate();

        eventHub.emit(new PageAcceptedEvent());

        setGamePause(false);
      } else {
        eventHub.emit(new PageAcceptedEvent());

        isFullDiary = true;
        pageIndex = 0;
        showNewPage(guiDocument, diaryDocument);

        solaEcs.getSystems().forEach(system -> system.setActive(false));
        solaEcs.setWorld(new World(1));
      }
    });

    attributesSectionElement.events().keyPressed().on(guiKeyEvent -> {
      if (!showingPlayerMessage) {
        return;
      }

      if (guiKeyEvent.getKeyEvent().keyCode() == Key.SPACE.getCode() || guiKeyEvent.getKeyEvent().keyCode() == Key.RIGHT.getCode()) {
        if (playerAttributeContainer.getPagesCollectedCount() < DialogUtil.pages.length - 2) {
          pageStyles.removeStyle(visibilityVisibleStyle);
          pageStyles.invalidate();

          eventHub.emit(new PageAcceptedEvent());

          setGamePause(false);
        } else {
          eventHub.emit(new PageAcceptedEvent());

          isFullDiary = true;
          pageIndex = 0;
          showNewPage(guiDocument, diaryDocument);

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

          pageTextElement.setText(DialogUtil.getNextEnemyResponseText(playerAttributeContainer));
          pageStyles.addStyle(visibilityVisibleStyle);
          pageStyles.invalidate();

          attributesSectionElement.requestFocus();
          showingPlayerMessage = true;

          int pagesCollected = playerAttributeContainer.getPagesCollectedCount();

          if (playerAttributeContainer.getMaxPagesCollectedCount() <= pagesCollected) {
            pageIndex = pagesCollected;
            showNewPage(guiDocument, diaryDocument);
          }
        }
      );
    });
  }

  private void initializeDiaryGui(GuiDocument guiDocument, GuiJsonDocument diaryDocument, GuiJsonDocument inGameDocument) {
    var diaryTextElement = diaryDocument.rootElement().findElementById("diary", TextGuiElement.class);
    var diaryContainerElement = diaryDocument.rootElement().findElementById("diary-container", SectionGuiElement.class);

    diaryTextElement.setText(DialogUtil.pages[pageIndex]);

    final Runnable changePage = () -> {
      if (pageIndex > DialogUtil.pages.length - 1) {
        pageIndex = DialogUtil.pages.length - 1;
      } else if (pageIndex < 0) {
        pageIndex = 0;
      }

      diaryTextElement.setText(DialogUtil.pages[pageIndex]);
    };

    diaryContainerElement.events().keyPressed().on(guiKeyEvent -> {
      if (isFullDiary) {
        if (guiKeyEvent.getKeyEvent().keyCode() == Key.RIGHT.getCode() || guiKeyEvent.getKeyEvent().keyCode() == Key.SPACE.getCode()) {
          pageIndex++;
          changePage.run();
        } else if (guiKeyEvent.getKeyEvent().keyCode() == Key.LEFT.getCode()) {
          pageIndex--;
          changePage.run();
        }
      } else {
        if (guiKeyEvent.getKeyEvent().keyCode() == Key.RIGHT.getCode() || guiKeyEvent.getKeyEvent().keyCode() == Key.SPACE.getCode()) {
          guiDocument.setRootElement(inGameDocument.rootElement());
        }
      }
    });

    diaryContainerElement.events().mousePressed().on(guiMouseEvent -> {
      if (isFullDiary) {
        pageIndex++;
        changePage.run();
      } else {
        guiDocument.setRootElement(inGameDocument.rootElement());
      }
    });
  }

  private void showNewPage(GuiDocument guiDocument, GuiJsonDocument diaryDocument) {
    var diaryTextElement = diaryDocument.rootElement().findElementById("diary", TextGuiElement.class);

    diaryTextElement.setText(DialogUtil.pages[pageIndex]);
    guiDocument.setRootElement(diaryDocument.rootElement());
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

    if (value < 2) {
      textGuiElement.styles().addStyle(negativeTextStyle);
    } else if (value > 4) {
      textGuiElement.styles().addStyle(positiveTextStyle);
    }

    textGuiElement.styles().invalidate();
  }
}
