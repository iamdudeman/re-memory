package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.rememory.Constants;

public class ForestRoomWorld extends RoomWorld {
  public ForestRoomWorld(String previousRoomId, int rendererWidth, int rendererHeight) {
    super(previousRoomId, rendererWidth, rendererHeight);


    for (int i = 0; i < rendererWidth; i += 8) {
      for (int j = 0; j < rendererHeight; j += 8) {
        int grassTileIndex = random.nextInt(3) + 1;
        String sprite = "grass_" + grassTileIndex;

        createEntity(
          new TransformComponent(i, j),
          new SpriteComponent(Constants.Assets.Sprites.ID, sprite)
        );
      }
    }

    for (int i = 0; i < 200; i++) {
      int x = random.nextInt(rendererWidth - 20) + 10;
      int y = random.nextInt(rendererHeight - 20) + 10;

      if (x > 40 && x < 200 && y < 40 && y > 0) {
        continue;
      }

      createEntity(
        new TransformComponent(x, y),
        new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TREE),
        new LayerComponent(Constants.Layers.DECORATION),
        new BlendModeComponent(BlendMode.MASK)
      );
    }

    int x = random.nextInt(rendererWidth - 20) + 10;
    int y = random.nextInt(rendererHeight - 20) + 10;

    addBoundaries(-boundarySize);

    addInitialPortal(x, y, false);

    addPlayer(rendererWidth * 0.5f, rendererHeight * 0.5f);
  }
}
