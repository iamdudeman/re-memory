package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.attributes.ReMemoryMaker;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

public class InitialRoomWorld extends RoomWorld {
  public InitialRoomWorld(String previousRoomId, int rendererWidth, int rendererHeight, ReMemoryMaker reMemoryMaker, EventHub eventHub) {
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
            new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.BACK_WALL_TOP),
            new LayerComponent(Constants.Layers.DECORATION)
          );
          createEntity(
            new TransformComponent(i, j + 8),
            new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.BACK_WALL_BOTTOM),
            new LayerComponent(Constants.Layers.DECORATION)
          );
        }
      }
    }

    float halfWidth = rendererWidth * 0.5f;
    float halfHeight = rendererHeight * 0.5f;

    addBoundaries(-boundarySize + 16);

    addInitialPortal(halfWidth, halfHeight, true);

    addPlayer(halfWidth, halfHeight - 10);

    float x = RandomUtils.quickRandomDoubleClamp(40, rendererWidth - 40, halfWidth - 10, halfWidth + 10);
    float y = RandomUtils.quickRandomDoubleClamp(40, rendererHeight - 40, halfHeight - 20, halfHeight + 10);

    createEntity(
      new TransformComponent(x, y),
      ColliderComponent.circle(4).setTags(Constants.Tags.BOUNDARY),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TABLE),
      new LayerComponent(Constants.Layers.DECORATION)
    );

    createEntity(
      new TransformComponent(x + 2, y + 1),
      ColliderComponent.aabb(-3, -4, 10, 10).setSensor(true),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PAGE),
      new LayerComponent(Constants.Layers.OBJECTS),
      new PageComponent(reMemoryMaker.createPage())
    );

    addTorch(
      RandomUtils.quickRandomDoubleClamp(x - 30, x + 30, x - 4, x + 4),
      RandomUtils.quickRandomDoubleClamp(y - 30, y + 30, y - 4, y + 4)
    );

    eventHub.add(PageAcceptedEvent.class, event -> {
      addLapis(
        RandomUtils.quickRandomDoubleClamp(x - 20, x + 20, x - 4, x + 4),
        RandomUtils.quickRandomDoubleClamp(y - 20, y + 20, y - 4, y + 4)
      );
    });
  }
}
