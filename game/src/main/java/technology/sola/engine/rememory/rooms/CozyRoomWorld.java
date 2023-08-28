package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.attributes.ReMemoryMaker;
import technology.sola.engine.rememory.components.PageComponent;

public class CozyRoomWorld extends RoomWorld {
  private final ReMemoryMaker reMemoryMaker;

  public CozyRoomWorld(String previousRoomId, int rendererWidth, int rendererHeight, ReMemoryMaker reMemoryMaker) {
    super(previousRoomId, rendererWidth, rendererHeight);
    this.reMemoryMaker = reMemoryMaker;

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

    addExtras();

    addBoundaries(-boundarySize + 16);

    addInitialPortal(halfWidth, halfHeight, true);

    addPlayer(halfWidth, halfHeight - 10);
  }

  private void addExtras() {
    float halfWidth = rendererWidth * 0.5f;
    float halfHeight = rendererHeight * 0.5f;
    boolean hasSpawnedPage = false;

    int tableCount = random.nextInt(4);

    for (int i = 0; i < tableCount; i++) {
      float x = RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10);
      float y = RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10);

      createEntity(
        new TransformComponent(x, y),
        ColliderComponent.circle(4).setTags(Constants.Tags.BOUNDARY),
        new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TABLE),
        new LayerComponent(Constants.Layers.DECORATION)
      );

      if (!hasSpawnedPage && random.nextInt(10) < 3) {
        createEntity(
          new TransformComponent(x + 2, y + 1),
          ColliderComponent.aabb(-3, -6, 6, 12).setSensor(true),
          new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PAGE),
          new LayerComponent(Constants.Layers.OBJECTS),
          new PageComponent(reMemoryMaker.createPage())
        );

        hasSpawnedPage = true;
      }
    }

    int lapisChance = 5;

    if (tableCount < 2) {
      lapisChance += 15;
    }
    if (!hasSpawnedPage) {
      lapisChance += 50;
    }

    if (RandomUtils.roll100() < lapisChance) {
      createEntity(
        new TransformComponent(
          RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
          RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 10, halfHeight + 10)
        ),
        ColliderComponent.aabb(2, 2).setSensor(true).setTags(Constants.Tags.LAPIS),
        new LayerComponent(Constants.Layers.OBJECTS),
        new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.LAPIS)
      );
    }

    addTorch(
      RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
      RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
    );

    addEnemy(
      RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
      RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
    );
    addEnemy(
      RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
      RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
    );
  }
}
