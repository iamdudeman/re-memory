package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class GuiManager {
  private final SolaGuiDocument document;
  private final SolaEcs solaEcs;
  private final GuiElement<?> attributesGui;
  private final GuiElement<?> playerMessageGui;
  private final GuiElement<?> diaryGui;
  private final GuiElement<?> controlsGui;

  public GuiManager(SolaGuiDocument document, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer, SolaEcs solaEcs) {
    this.document = document;
    this.solaEcs = solaEcs;
    attributesGui = AttributesGui.build(document, eventHub, playerAttributeContainer, solaEcs);
    playerMessageGui = PlayerMessageGui.build(this::changeGui, this::setGamePause, document, eventHub, solaEcs, playerAttributeContainer);
    diaryGui = DiaryGui.build(document);
    controlsGui = ControlsGui.build(this::changeGui, this::setGamePause, document);

    document.setGuiRoot(attributesGui);

    changeGui(0);
  }

  private void setGamePause(boolean isPaused) {
    solaEcs.getSystem(EnemySystem.class).setActive(!isPaused);
    solaEcs.getSystem(PlayerSystem.class).setActive(!isPaused);
    solaEcs.getSystem(PhysicsSystem.class).setActive(!isPaused);
  }

  private void changeGui(int id) {
    if (id == 0) {
      document.setGuiRoot(controlsGui, 10, 10);
    } else if (id == 1) {
      document.setGuiRoot(attributesGui);
    } else if (id == 2) {
      document.setGuiRoot(playerMessageGui, 1, 190);
    } else if (id == 3) {
      document.setGuiRoot(diaryGui, 30, 5);
    }
  }
}
