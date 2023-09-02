package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.rememory.PlayerAttributeContainer;

public class GuiManager {
  private final SolaGuiDocument document;
  private final GuiElement<?> attributesGui;
  private final GuiElement<?> playerMessageGui;
  private final GuiElement<?> diaryGui;

  public GuiManager(SolaGuiDocument document, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer, SolaEcs solaEcs) {
    this.document = document;
    attributesGui = AttributesGui.build(document, eventHub, playerAttributeContainer, solaEcs);
    playerMessageGui = PlayerMessageGui.build(this::changeGui, document, eventHub, solaEcs, playerAttributeContainer);
    diaryGui = DiaryGui.build(document);

    document.setGuiRoot(attributesGui);

    changeGui(2);
  }

  private void changeGui(int id) {
    if (id == 1) {
      document.setGuiRoot(attributesGui);
    } else if (id == 2) {
      document.setGuiRoot(playerMessageGui, 1, 190);
    } else if (id == 3) {
      document.setGuiRoot(diaryGui, 30, 5);
    }
  }
}
