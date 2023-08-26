package technology.sola.engine;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.rememory.Constants;

import java.util.Random;

public class InitialRooms {
  public static World buildFirstRoom() {
    // todo
    return new World(5);
  }

  public static World buildForest(int rendererWidth, int rendererHeight) {
    Random random = new Random();
    World world = new World(1500);

    for (int i = 0; i < rendererWidth; i += 8) {
      for (int j = 0; j < rendererHeight; j += 8) {
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
      int x = random.nextInt(rendererWidth - 20) + 10;
      int y = random.nextInt(rendererHeight - 20) + 10;

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
