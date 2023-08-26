package technology.sola.engine.rememory;

import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.rememory.systems.PlayerSystem;

import java.util.Random;

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
    solaEcs.setWorld(buildWorld());
  }

  private World buildWorld() {
    Random random = new Random();
    World world = new World(1500);

    for (int i = 0; i < platform.getRenderer().getWidth(); i += 8) {
      for (int j = 0; j < platform.getRenderer().getHeight(); j += 8) {
        String sprite;

        if (i >= 40 && i <= 200 && j <= 40 && j >= 0) {
          sprite = Constants.Assets.Sprites.WOOD_1;
        } else {
          int grassTileIndex = random.nextInt(3) + 1;
          sprite = "grass_" + grassTileIndex;
        }

        world.createEntity(
          new TransformComponent(i, j),
          new SpriteComponent(Constants.Assets.Sprites.ID_SHEET, sprite)
        );
      }
    }

    for (int i = 0; i < 200; i++) {
      int x = random.nextInt(platform.getRenderer().getWidth() - 20) + 10;
      int y = random.nextInt(platform.getRenderer().getHeight() - 20) + 10;

      if (x > 40 && x < 200 && y < 40 && y > 0) {
        continue;
      }

      world.createEntity(
        new TransformComponent(x, y),
        new SpriteComponent(Constants.Assets.Sprites.ID_SHEET, Constants.Assets.Sprites.TREE),
        new LayerComponent(Constants.Layers.OBJECTS),
        new BlendModeComponent(BlendMode.MASK)
      );
    }

    world.createEntity(
      new TransformComponent(100, 0),
      new SpriteComponent(Constants.Assets.Sprites.ID_SHEET, Constants.Assets.Sprites.DOOR_TOP),
      new LayerComponent(Constants.Layers.OBJECTS),
      new BlendModeComponent(BlendMode.MASK)
    );

    world.createEntity(
        new TransformComponent(100, 100, 1, 1),
        new LayerComponent(Constants.Layers.OBJECTS, 2),
        new BlendModeComponent(BlendMode.MASK),
        new LightComponent(50, new Color(200, 255, 255, 255)).setOffset(2.5f, 4),
        new SpriteComponent(Constants.Assets.Sprites.ID_SHEET, Constants.Assets.Sprites.PLAYER)
      ).setName(Constants.Names.PLAYER);

    return world;
  }
}
