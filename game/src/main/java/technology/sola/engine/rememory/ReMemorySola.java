package technology.sola.engine.rememory;

import technology.sola.engine.InitialRooms;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.rememory.systems.PlayerSystem;

public class ReMemorySola extends SolaWithDefaults {
  public ReMemorySola() {
    super(SolaConfiguration.build("re;memory", 256, 240).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().useLighting(new Color(10, 10, 10)).useBackgroundColor(Color.WHITE);

    assetLoaderProvider.get(SpriteSheet.class)
      .addAssetMapping("main", "assets/rememory_spritesheet.json");

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
    platform.getRenderer().createLayers(
      Constants.Layers.OBJECTS
    );

    solaEcs.addSystems(new PlayerSystem(keyboardInput));
    solaEcs.setWorld(InitialRooms.buildForest(platform.getRenderer().getWidth(), platform.getRenderer().getHeight()));
  }
}
