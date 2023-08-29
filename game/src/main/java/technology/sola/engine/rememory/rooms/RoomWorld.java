package technology.sola.engine.rememory.rooms;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.*;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.EnemyComponent;
import technology.sola.engine.rememory.components.PortalComponent;

import java.util.Random;

public abstract class RoomWorld extends World {
  protected static final float boundarySize = 50;
  protected Random random = new Random();
  protected int rendererWidth;
  protected int rendererHeight;
  private final String previousRoomId;

  public RoomWorld(String previousRoomId, int rendererWidth, int rendererHeight) {
    super(1500);
    this.previousRoomId = previousRoomId;
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
  }

  public void addPortal(float x, float y) {
    createEntity(
      new PortalComponent(null, true),
      new TransformComponent(x, y, 8),
      ColliderComponent.circle().setSensor(true).setTags(Constants.Tags.PORTAL),
      new CircleRendererComponent(new Color(177, 156, 217), true), // TODO temp until particle layer works
      new LayerComponent(Constants.Layers.OBJECTS)
    );
  }

  protected void addPlayer(float x, float y) {
    createEntity(
      new DynamicBodyComponent(),
      new TransformComponent(x, y),
      new LayerComponent(Constants.Layers.OBJECTS, 2),
      new BlendModeComponent(BlendMode.MASK),
      new LightComponent(200, new Color(200, 255, 255, 255)).setOffset(2.5f, 4),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PLAYER),
      ColliderComponent.aabb(5, 7).setTags(Constants.Tags.PLAYER)
    ).setName(Constants.Names.PLAYER);
  }

  protected void addInitialPortal(float x, float y, boolean delayActivation) {
    createEntity(
      new PortalComponent(previousRoomId, delayActivation),
      new TransformComponent(x, y, 8),
      ColliderComponent.circle().setSensor(true).setTags(Constants.Tags.PORTAL),
      new CircleRendererComponent(new Color(177, 156, 217), true), // TODO temp until particle layer works
      new LayerComponent(Constants.Layers.OBJECTS)
    );
  }

  protected void addEnemy(float x, float y) {
    createEntity(
      new TransformComponent(x, y,
        4 // todo temp until sprite
      ),
      new DynamicBodyComponent(),
      new EnemyComponent(random.nextBoolean() ? EnemyComponent.EnemyType.CREEPER : EnemyComponent.EnemyType.SPOOKER),
      ColliderComponent.circle().setIgnoreTags(Constants.Tags.BOUNDARY), // todo temp until sprite (need to set radius then)
      new CircleRendererComponent(Color.BLACK), // todo temp until sprite
//      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.ENEMY),
      new LayerComponent(Constants.Layers.OBJECTS, 1)
    );
  }

  protected void addTorch(float x, float y) {
    createEntity(
      new TransformComponent(x, y),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TORCH),
      new LayerComponent(Constants.Layers.OBJECTS, 1),
      new LightComponent(16, Color.YELLOW)
        .setOffset(1.5f, 3)
        .setLightFlicker(new LightFlicker(0.2f, 0.8f))
    );
  }

  protected void addBoundaries(float topBoundaryY) {
    // top
    createEntity(
      new TransformComponent(0, topBoundaryY, rendererWidth, boundarySize),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
    // bottom
    createEntity(
      new TransformComponent(0, rendererHeight, rendererWidth, boundarySize),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
    // left
    createEntity(
      new TransformComponent(-boundarySize, 0, boundarySize, rendererHeight),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
    // right
    createEntity(
      new TransformComponent(rendererWidth, 0, boundarySize, rendererHeight),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
  }
}
