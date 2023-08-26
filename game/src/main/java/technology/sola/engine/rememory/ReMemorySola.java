package technology.sola.engine.rememory;

import technology.sola.engine.InitialRooms;
import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class ReMemorySola extends SolaWithDefaults {
  public ReMemorySola() {
    super(SolaConfiguration.build("re;memory", 256, 240).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.usePhysics().useGraphics().useLighting(new Color(10, 10, 10)).useBackgroundColor(Color.WHITE);

    solaEcs.getSystem(GravitySystem.class).setActive(false);

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
    platform.getRenderer().createLayers(
      Constants.Layers.OBJECTS
    );

    solaEcs.addSystems(
      new PlayerSystem(keyboardInput),
      new ParticleSystem()
    );
    solaEcs.setWorld(InitialRooms.buildForest(platform.getRenderer().getWidth(), platform.getRenderer().getHeight()));
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(SpriteSheet.class, Constants.Assets.Sprites.ID, "assets/rememory_spritesheet.json")
      .addAsset(SpriteSheet.class, Constants.Assets.CozySprites.ID, "assets/cozy_room.json")
      .loadAll()
        .onComplete(assets -> {
          completeAsyncInit.run();
        });
  }
}
