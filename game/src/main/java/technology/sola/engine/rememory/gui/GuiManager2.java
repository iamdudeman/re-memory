package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiDocument;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.events.AttributesChangedEvent;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class GuiManager2 {
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

  public GuiManager2(SolaEcs solaEcs, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer) {
    this.solaEcs = solaEcs;
    this.eventHub = eventHub;
    this.playerAttributeContainer = playerAttributeContainer;
  }

  public void initialize(GuiDocument guiDocument, GuiJsonDocument controlsDocument, GuiJsonDocument inGameDocument, GuiJsonDocument diaryDocument) {
    guiDocument.setRootElement(controlsDocument.rootElement());

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

    eventHub.add(AttributesChangedEvent.class, event -> {
      updateAttributeValueText(inGameDocument.rootElement().findElementById("speed", TextGuiElement.class), playerAttributeContainer.getSpeed());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("stealth", TextGuiElement.class), playerAttributeContainer.getStealth());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("vision", TextGuiElement.class), playerAttributeContainer.getVision());
      updateAttributeValueText(inGameDocument.rootElement().findElementById("pages", TextGuiElement.class), playerAttributeContainer.getPagesCollectedCount());
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
}
