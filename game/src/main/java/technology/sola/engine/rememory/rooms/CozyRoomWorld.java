package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.rememory.Constants;

public class CozyRoomWorld extends RoomWorld {
  public CozyRoomWorld(String previousRoomId, int rendererWidth, int rendererHeight) {
    super(previousRoomId, rendererWidth, rendererHeight);

    for (int i = 0; i < rendererWidth; i += 16) {
      for (int j = 0; j < rendererHeight; j += 16) {
        createEntity(
          new TransformComponent(i, j),
          new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.FLOOR)
        );

        if (j == 0) {
          createEntity(
            new TransformComponent(i, j),
            new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.BACK_WALL_TOP)
          );
          createEntity(
            new TransformComponent(i, j + 8),
            new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.BACK_WALL_BOTTOM),
            ColliderComponent.aabb(16, 8)
          );
        }
      }
    }

    addBoundaries();

    addInitialPortal(rendererWidth * 0.5f, rendererHeight * 0.5f, true);

    addPlayer(rendererWidth * 0.5f, rendererHeight * 0.5f - 10);
  }
}
