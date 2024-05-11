package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.components.EnemyComponent;
import technology.sola.engine.rememory.components.PageComponent;

public class CozyRoomWorld extends RoomWorld {
  public CozyRoomWorld(int rendererWidth, int rendererHeight, PlayerAttributeContainer playerAttributeContainer) {
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

    addEnemies(playerAttributeContainer);

    addExtras(playerAttributeContainer);

    addBoundaries(-boundarySize + 16);

    addInitialPortal(halfWidth, halfHeight, true);

    addPlayer(halfWidth, halfHeight - 10);
  }

  private void addExtras(PlayerAttributeContainer playerAttributeContainer) {
    float halfWidth = rendererWidth * 0.5f;
    float halfHeight = rendererHeight * 0.5f;
    boolean hasSpawnedPage = false;
    boolean hasNook = false;

    if (random.nextBoolean()) {
      addNook(10, 20);
      hasNook = true;
    }

    for (int i = 0; i < 10; i++) {
      if (random.nextBoolean()) {
        addShelf(
          RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
          RandomUtils.quickRandomDoubleClamp(hasNook ? 40 : 20, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
        );
      }
    }

    int tableCount = random.nextInt(4);

    for (int i = 0; i < tableCount; i++) {
      float x = RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10);
      float y = RandomUtils.quickRandomDoubleClamp(hasNook ? 40 : 18, rendererHeight - 20, halfHeight - 20, halfHeight + 10);

      createEntity(
        new TransformComponent(x, y),
        new ColliderComponent(new ColliderShapeCircle(4)).setTags(Constants.Tags.BOUNDARY),
        new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TABLE),
        new LayerComponent(Constants.Layers.DECORATION)
      );

      boolean delayPageSpawn = playerAttributeContainer.getPagesCollectedCount() > 3 && playerAttributeContainer.getStatCount() < 8;

      if (!hasSpawnedPage && random.nextBoolean() && !delayPageSpawn) {
        createEntity(
          new TransformComponent(x + 2, y + 1),
          new ColliderComponent(new ColliderShapeAABB(10, 10), -3, -4).setSensor(true),
          new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PAGE),
          new LayerComponent(Constants.Layers.OBJECTS),
          new PageComponent()
        );

        hasSpawnedPage = true;
      }
    }

    int lapisChance = 20;

    if (playerAttributeContainer.getStatCount() < 6) {
      lapisChance += 30;
    }

    if (tableCount < 2) {
      lapisChance += 15;
    }
    if (!hasSpawnedPage) {
      lapisChance += 50;
    }

    if (RandomUtils.roll100() < lapisChance) {
      addLapis(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(hasNook ? 40 : 18, rendererHeight - 20, halfHeight - 10, halfHeight + 10)
      );
    }
    if (RandomUtils.roll100() < 20) {
      addLapis(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(hasNook ? 40 : 18, rendererHeight - 20, halfHeight - 10, halfHeight + 10)
      );
    }

    if (RandomUtils.roll100() < 15) {
      addDuck(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(hasNook ? 40 : 18, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
      );
    }

    if (playerAttributeContainer.getPagesCollectedCount() != 2) {
      addTorch(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(hasNook ? 40 : 18, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
      );
    }
  }

  private void addEnemies(PlayerAttributeContainer playerAttributeContainer) {
    float halfWidth = rendererWidth * 0.5f;
    float halfHeight = rendererHeight * 0.5f;

    addEnemy(
      RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
      RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
      EnemyComponent.EnemyType.CREEPER
    );

    if (RandomUtils.roll100() < 25) {
      addEnemy(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
        EnemyComponent.EnemyType.CREEPER
      );
    }

    if (playerAttributeContainer.getPagesCollectedCount() > 1) {
      addEnemy(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
        EnemyComponent.EnemyType.CREEPER
      );
    }

    if (playerAttributeContainer.getPagesCollectedCount() > 2) {
      addEnemy(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
        random.nextBoolean() ? EnemyComponent.EnemyType.SPOOKER : EnemyComponent.EnemyType.CREEPER
      );
    }

    if (playerAttributeContainer.getPagesCollectedCount() > 3) {
      addEnemy(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
        EnemyComponent.EnemyType.SPOOKER
      );
    }

    if (playerAttributeContainer.getStatCount() > 10) {
      addEnemy(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
        EnemyComponent.EnemyType.SPOOKER
      );
    } else if (playerAttributeContainer.getStatCount() > 5) {
      addEnemy(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
        EnemyComponent.EnemyType.CREEPER
      );
    }
  }

  private void addNook(float x, float y) {
    // left
    if (RandomUtils.roll100() < 60) {
      addShelf(x, y + 22);
    }

    // top 1
    if (RandomUtils.roll100() < 30) {
      addShelf(x + 5, y);
    }

    // top 2
    addShelf(x + 5 + 18, y);

    // top 3
    addShelf(x + 5 + 18 + 18, y);

    // right
    if (RandomUtils.roll100() < 50) {
      addShelf(x + 5 + 18 + 18 + 5, y + 22);
    }

    addTorch(x + 5 + 11, y + 16);

    createEntity(
      new TransformComponent(x + 5 + 18 - 3, y + 18 + 2),
      new SpriteComponent(Constants.Assets.LibrarySprites.ID, Constants.Assets.LibrarySprites.TABLE_SET),
      new ColliderComponent(new ColliderShapeAABB(17, 3), 0, 2).setTags(Constants.Tags.BOUNDARY),
      new LayerComponent(Constants.Layers.OBJECTS, 3)
    );
  }

  private void addShelf(float x, float y) {
    createEntity(
      new TransformComponent(x, y),
      new SpriteComponent(Constants.Assets.LibrarySprites.ID, Constants.Assets.LibrarySprites.SHELF_1),
      new ColliderComponent(new ColliderShapeAABB(11, 6), 0, 5).setTags(Constants.Tags.BOUNDARY),
      new LayerComponent(Constants.Layers.OBJECTS, 3)
    );
  }
}
