package technology.sola.engine.rememory.gui;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.graphics.guiv2.GuiDocument;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class GuiManager2 {
  private final SolaEcs solaEcs;

  public GuiManager2(SolaEcs solaEcs) {
    this.solaEcs = solaEcs;
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
  }

  private void setGamePause(boolean isPaused) {
    solaEcs.getSystem(EnemySystem.class).setActive(!isPaused);
    solaEcs.getSystem(PlayerSystem.class).setActive(!isPaused);
    solaEcs.getSystem(PhysicsSystem.class).setActive(!isPaused);
  }
}
