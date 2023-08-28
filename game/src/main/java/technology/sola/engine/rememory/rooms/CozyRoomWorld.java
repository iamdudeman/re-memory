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

  public CozyRoomWorld(String previousRoomId, int rendererWidth, int rendererHeight, ReMemoryMaker reMemoryMaker) {
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

    // todo probably needs to be a random chance instead
    createEntity(
      new TransformComponent(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 10, halfHeight + 10)

//        random.nextInt(10, rendererWidth - 20),
//        random.nextInt(20, rendererHeight - 20)
      ),
      ColliderComponent.aabb(2, 2).setSensor(true).setTags(Constants.Tags.LAPIS),
      new LayerComponent(Constants.Layers.OBJECTS),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.LAPIS)
    );

    // todo probably needs to be a random chance instead
    createEntity(
      new TransformComponent(
        RandomUtils.quickRandomDoubleClamp(10, rendererWidth - 20, halfWidth - 10, halfWidth + 10),
        RandomUtils.quickRandomDoubleClamp(10, rendererHeight - 20, halfHeight - 10, halfHeight + 10)
      ),
      ColliderComponent.aabb(3, 6).setSensor(true),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PAGE),
      new LayerComponent(Constants.Layers.OBJECTS),
      new PageComponent(reMemoryMaker.createPage())
    );

    addBoundaries(-boundarySize + 16);

    addInitialPortal(halfWidth, halfHeight, true);

    addPlayer(halfWidth, halfHeight - 10);
  }
}
