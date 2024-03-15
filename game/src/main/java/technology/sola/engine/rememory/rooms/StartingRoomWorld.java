package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.RandomUtils;

public class StartingRoomWorld extends RoomWorld {
  public StartingRoomWorld(int rendererWidth, int rendererHeight) {
    super(rendererWidth, rendererHeight);


    for (int i = 0; i < rendererWidth; i += 8) {
      for (int j = 0; j < rendererHeight; j += 8) {
        int grassTileIndex = random.nextInt(3) + 1;
        String sprite = switch (grassTileIndex) {
          case 1 -> Constants.Assets.Sprites.GRASS_1;
          case 2 -> Constants.Assets.Sprites.GRASS_2;
          default -> Constants.Assets.Sprites.GRASS_3;
        };

        createEntity(
          new TransformComponent(i, j),
          new SpriteComponent(Constants.Assets.Sprites.ID, sprite)
        );
      }
    }

    for (int i = 0; i < 40; i++) {
      int x = random.nextInt(rendererWidth - 20) + 10;
      int y = random.nextInt(rendererHeight - 20) + 10;

      createEntity(
        new TransformComponent(x, y, 2),
        new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TREE),
        new LayerComponent(Constants.Layers.OBJECTS, 3),
        new BlendModeComponent(BlendMode.MASK)
      );

      if (RandomUtils.roll100() < 20) {
        createEntity(
          new TransformComponent(
            x + 1, y + 10, 0.5f, 0.5f
          ),
          new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.BERRY_BUSH),
          new LayerComponent(Constants.Layers.OBJECTS, 4),
          new BlendModeComponent(BlendMode.MASK)
        );
      }
    }

    float halfWidth = rendererWidth * 0.5f;
    float halfHeight = rendererHeight * 0.5f;
    float x = RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10);
    float y = RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 10, halfHeight + 10);

    addInitialPortal(x, y, false);

    addTorch(
      RandomUtils.quickRandomDoubleClamp(
        Math.max(x - 40, 20), Math.min(x + 40, rendererWidth - 20),
        x - 5, x + 5
      ),
      RandomUtils.quickRandomDoubleClamp(
        Math.max(y - 40, 20), Math.min(y + 40, rendererHeight - 20),
        y - 5, y + 5
      )
    );

    addBoundaries(-boundarySize);

    addPlayer(halfWidth, halfHeight);
  }
}
