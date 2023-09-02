package technology.sola.engine.rememory.rooms;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.LightFlicker;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.components.EnemyComponent;
import technology.sola.engine.rememory.components.PageComponent;

public class CozyRoomWorld extends RoomWorld {

  public CozyRoomWorld(String previousRoomId, int rendererWidth, int rendererHeight, PlayerAttributeContainer playerAttributeContainer) {
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

    addExtras(playerAttributeContainer);

    addBoundaries(-boundarySize + 16);

    addInitialPortal(halfWidth, halfHeight, true);

    addPlayer(halfWidth, halfHeight - 10);
  }

  private void addExtras(PlayerAttributeContainer playerAttributeContainer) {
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

      if (!hasSpawnedPage && random.nextBoolean()) {
        createEntity(
          new TransformComponent(x + 2, y + 1),
          ColliderComponent.aabb(-3, -4, 10, 10).setSensor(true),
          new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PAGE),
          new LayerComponent(Constants.Layers.OBJECTS),
          new PageComponent()
        );

        if (playerAttributeContainer.getPagesCollectedCount() == 2) {
          createEntity(
            new TransformComponent(
              RandomUtils.quickRandomDoubleClamp(x - 30, x + 30, x - 5, x + 5),
              RandomUtils.quickRandomDoubleClamp(y - 30, y + 30, y - 5, y + 15)
            ),
            new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.LIGHTHOUSE),
            ColliderComponent.aabb(0, 6, 3, 6).setTags(Constants.Tags.BOUNDARY),
            new LayerComponent(Constants.Layers.OBJECTS, 3),
            new LightComponent(64, Color.WHITE)
              .setOffset(1f, 3)
              .setLightFlicker(new LightFlicker(0.5f, 0.9f))
          );
        }

        hasSpawnedPage = true;
      }
    }

    int lapisChance = 15;

    if (tableCount < 2) {
      lapisChance += 15;
    }
    if (!hasSpawnedPage) {
      lapisChance += 50;
    }

    if (RandomUtils.roll100() < lapisChance) {
      addLapis(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 10, halfHeight + 10)
      );
    }
    if (RandomUtils.roll100() < lapisChance - 20) {
      addLapis(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 10, halfHeight + 10)
      );
    }

    if (RandomUtils.roll100() < 15) {
      addDuck(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
      );
    }

    if (RandomUtils.roll100() < 30) {
      addDonut(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
      );
    }

    if (playerAttributeContainer.getPagesCollectedCount() != 2) {
      addTorch(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10)
      );
    }

    addEnemy(
      RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
      RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 20, halfHeight + 10),
      EnemyComponent.EnemyType.CREEPER
    );

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
        random.nextBoolean() || playerAttributeContainer.getPagesCollectedCount() > 3 ? EnemyComponent.EnemyType.SPOOKER : EnemyComponent.EnemyType.CREEPER
      );
    }
  }
}
