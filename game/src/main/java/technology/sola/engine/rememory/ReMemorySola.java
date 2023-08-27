package technology.sola.engine.rememory;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.rememory.events.ForgetWhoEvent;
import technology.sola.engine.rememory.attributes.PlayerAttributeContainer;
import technology.sola.engine.rememory.gui.GameGui;
import technology.sola.engine.rememory.systems.RoomSystem;
import technology.sola.engine.rememory.systems.PlayerSystem;
import technology.sola.engine.rememory.systems.PortalSystem;

public class ReMemorySola extends SolaWithDefaults {
  public ReMemorySola() {
    super(SolaConfiguration.build("re;memory", 256, 240).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui(new GuiPropertyDefaults("monospaced_NORMAL_10", Color.BLACK, Color.BLANK))
      .usePhysics()
      .useGraphics()
//      .useLighting()
      .useLighting(new Color(10, 10, 10))
      .useBackgroundColor(Color.WHITE);

    // default physics overrides
    solaEcs.getSystem(GravitySystem.class).setActive(false);
    solaEcs.addSystem(
      new ImpulseCollisionResolutionSystem(eventHub, 15, 0.001f, 0.7f)
    );

    // rendering stuff
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
    platform.getRenderer().createLayers(
      Constants.Layers.OBJECTS
    );

    PlayerAttributeContainer playerAttributeContainer = new PlayerAttributeContainer(eventHub);

    // ecs
    solaEcs.addSystems(
      new PlayerSystem(keyboardInput, eventHub, playerAttributeContainer),
      new ParticleSystem(),
      new RoomSystem(eventHub, platform.getRenderer(), solaEcs),
      new PortalSystem(eventHub)
    );

    // gui
    solaGuiDocument.setGuiRoot(GameGui.build(solaGuiDocument, playerAttributeContainer, eventHub));

    eventHub.emit(new ForgetWhoEvent());
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(AudioClip.class, "time", "assets/time.wav")
      .addAsset(Font.class, "monospaced_NORMAL_10", "assets/monospaced_NORMAL_10.json")
      .addAsset(SpriteSheet.class, Constants.Assets.Sprites.ID, "assets/rememory_spritesheet.json")
      .addAsset(SpriteSheet.class, Constants.Assets.CozySprites.ID, "assets/cozy_room.json")
      .loadAll()
        .onComplete(assets -> {
          AudioClip audioClip = ((AudioClip)assets[0]);
          audioClip.setVolume(0.5f);
          audioClip.loop(-1);
          completeAsyncInit.run();
        });
  }
}
