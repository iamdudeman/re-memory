package technology.sola.engine.rememory.rooms;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.*;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.EnemyComponent;
import technology.sola.engine.rememory.components.PortalComponent;

import java.util.Random;

public abstract class RoomWorld extends World {
  protected static final float boundarySize = 50;
  protected Random random = new Random();
  protected int rendererWidth;
  protected int rendererHeight;

  public RoomWorld(int rendererWidth, int rendererHeight) {
    super(1500);
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
  }

  protected void addPlayer(float x, float y) {
    createEntity(
      new DynamicBodyComponent(),
      new TransformComponent(x, y),
      new LayerComponent(Constants.Layers.OBJECTS, 2),
      new BlendModeComponent(BlendMode.MASK),
      new LightComponent(200, new Color(200, 255, 255, 255)).setOffset(2.5f, 4),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PLAYER),
      new ColliderComponent(new ColliderShapeAABB(5, 7), 2, 1).setTags(Constants.Tags.PLAYER)
    ).setName(Constants.Names.PLAYER);
  }

  protected void addInitialPortal(float x, float y, boolean delayActivation) {
    createEntity(
      new PortalComponent(delayActivation),
      new TransformComponent(x, y, 8),
      new ColliderComponent(new ColliderShapeCircle()).setSensor(true).setTags(Constants.Tags.PORTAL),
      new CircleRendererComponent(new Color(177, 156, 217), true),
      new LightComponent(16, new Color(177, 156, 217, 120)).setLightFlicker(new LightFlicker(0.25f, 0.6f)).setOffset(3, 3),
      new BlendModeComponent(BlendMode.NORMAL),
      new LayerComponent(Constants.Layers.OBJECTS)
    );
  }

  protected void addLapis(float x, float y) {
    createEntity(
      new TransformComponent(x, y),
      new ColliderComponent(new ColliderShapeAABB(4, 4)).setSensor(true).setTags(Constants.Tags.LAPIS),
      new LayerComponent(Constants.Layers.OBJECTS, 4),
      new DynamicBodyComponent(),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.LAPIS)
    );
  }

  protected void addEnemy(float x, float y, EnemyComponent.EnemyType enemyType) {
    createEntity(
      new TransformComponent(x, y, 4),
      new DynamicBodyComponent(),
      new EnemyComponent(enemyType),
      new ColliderComponent(new ColliderShapeCircle()).setIgnoreTags(Constants.Tags.BOUNDARY),
      new CircleRendererComponent(Color.BLACK),
      new LayerComponent(Constants.Layers.OBJECTS, 1)
    );
  }

  protected void addEnemyContrast(float x, float y, EnemyComponent.EnemyType enemyType) {
    createEntity(
      new TransformComponent(x, y, 4),
      new DynamicBodyComponent(),
      new EnemyComponent(enemyType),
      new ColliderComponent(new ColliderShapeCircle()).setIgnoreTags(Constants.Tags.BOUNDARY),
      new CircleRendererComponent(new Color(158, 158, 158)),
      new LayerComponent(Constants.Layers.OBJECTS, 1)
    );
  }

  protected void addDuck(float x, float y) {
    createEntity(
      new TransformComponent(x, y),
      new DynamicBodyComponent(new Material(100, 0, 0), true),
      new ColliderComponent(new ColliderShapeCircle(4)).setTags(Constants.Tags.DUCK),
      new SpriteComponent(Constants.Assets.AcidRainSprites.ID, Constants.Assets.AcidRainSprites.DUCK),
      new LayerComponent(Constants.Layers.OBJECTS)
    );
  }

  protected void addTorch(float x, float y) {
    createEntity(
      new TransformComponent(x, y),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TORCH),
      new LayerComponent(Constants.Layers.OBJECTS, 1),
      new LightComponent(40, Color.YELLOW)
        .setOffset(1.5f, 3)
        .setLightFlicker(new LightFlicker(0.2f, 0.8f))
    );
  }

  protected void addBoundaries(float topBoundaryY) {
    // top
    createEntity(
      new TransformComponent(0, topBoundaryY, rendererWidth, boundarySize),
      new ColliderComponent(new ColliderShapeAABB()).setTags(Constants.Tags.BOUNDARY)
    );
    // bottom
    createEntity(
      new TransformComponent(0, rendererHeight, rendererWidth, boundarySize),
      new ColliderComponent(new ColliderShapeAABB()).setTags(Constants.Tags.BOUNDARY)
    );
    // left
    createEntity(
      new TransformComponent(-boundarySize, 0, boundarySize, rendererHeight),
      new ColliderComponent(new ColliderShapeAABB()).setTags(Constants.Tags.BOUNDARY)
    );
    // right
    createEntity(
      new TransformComponent(rendererWidth, 0, boundarySize, rendererHeight),
      new ColliderComponent(new ColliderShapeAABB()).setTags(Constants.Tags.BOUNDARY)
    );
  }
}
