package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.components.PageComponent;
import technology.sola.engine.rememory.events.PageAcceptedEvent;

public class InitialRoomWorld extends RoomWorld {
  private boolean isLapisAdded = false;

  public InitialRoomWorld(int rendererWidth, int rendererHeight, EventHub eventHub) {
    super(rendererWidth, rendererHeight);

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

    float x = RandomUtils.quickRandomDoubleClamp(40, rendererWidth - 40, halfWidth - 20, halfWidth + 20);
    float y = RandomUtils.quickRandomDoubleClamp(40, rendererHeight - 40, halfHeight - 30, halfHeight + 20);

    addBookShelves(x, y);

    float tableX = x + 5;
    float tableY = y + 21;

    createEntity(
      new TransformComponent(tableX, tableY),
      ColliderComponent.circle(4).setTags(Constants.Tags.BOUNDARY),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TABLE),
      new LayerComponent(Constants.Layers.DECORATION)
    );

    createEntity(
      new TransformComponent(tableX + 2, tableY + 1),
      ColliderComponent.aabb(-3, -4, 10, 10).setSensor(true),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PAGE),
      new LayerComponent(Constants.Layers.OBJECTS),
      new PageComponent()
    );

    addTorch(tableX - 30, tableY);

    eventHub.add(PageAcceptedEvent.class, event -> {
      if (!isLapisAdded) {
        isLapisAdded = true;
        addLapis(tableX + 22, tableY + 2);
      }
    });
  }

  private void addBookShelves(float x, float y) {
    createEntity(
      new TransformComponent(x, y),
      new SpriteComponent(Constants.Assets.LibrarySprites.ID, Constants.Assets.LibrarySprites.SHELF_1),
      ColliderComponent.aabb(0, 5, 11, 6).setTags(Constants.Tags.BOUNDARY),
      new LayerComponent(Constants.Layers.OBJECTS, 3)
    );

    createEntity(
      new TransformComponent(x + 18, y),
      new SpriteComponent(Constants.Assets.LibrarySprites.ID, Constants.Assets.LibrarySprites.SHELF_2),
      ColliderComponent.aabb(0, 5, 11, 6).setTags(Constants.Tags.BOUNDARY),
      new LayerComponent(Constants.Layers.OBJECTS, 3)
    );
  }
}
