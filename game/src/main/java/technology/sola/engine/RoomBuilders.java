package technology.sola.engine;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.*;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.PortalComponent;
import technology.sola.math.linear.Vector2D;

import java.util.Random;

public class RoomBuilders {
  private static final float boundarySize = 50;

  public static World buildCozy(int rendererWidth, int rendererHeight) {
    World world = new World(1500);

    for (int i = 0; i < rendererWidth; i += 16) {
      for (int j = 0; j < rendererHeight; j += 16) {
        world.createEntity(
          new TransformComponent(i, j),
          new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.FLOOR)
        );

        if (j == 0) {
          world.createEntity(
            new TransformComponent(i, j),
            new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.BACK_WALL_TOP)
          );
          world.createEntity(
            new TransformComponent(i, j + 8),
            new SpriteComponent(Constants.Assets.CozySprites.ID, Constants.Assets.CozySprites.BACK_WALL_BOTTOM),
            ColliderComponent.aabb(16, 8)
          );
        }
      }
    }

    addBoundaries(world, rendererWidth, rendererHeight);

    addPlayer(world);

    return world;
  }

  public static World buildForest(int rendererWidth, int rendererHeight) {
    Random random = new Random();
    World world = new World(1500);

    for (int i = 0; i < rendererWidth; i += 8) {
      for (int j = 0; j < rendererHeight; j += 8) {
        int grassTileIndex = random.nextInt(3) + 1;
        String sprite = "grass_" + grassTileIndex;

        world.createEntity(
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

      world.createEntity(
        new TransformComponent(x, y),
        new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.TREE),
        new LayerComponent(Constants.Layers.OBJECTS),
        new BlendModeComponent(BlendMode.MASK)
      );
    }

    int x = random.nextInt(rendererWidth - 20) + 10;
    int y = random.nextInt(rendererHeight - 20) + 10;

    addBoundaries(world, rendererWidth, rendererHeight);

    addPortal(world, x, y);

    addPlayer(world);

    return world;
  }

  private static void addBoundaries(World world, int rendererWidth, int rendererHeight) {
    world.createEntity(
      new TransformComponent(0, -boundarySize, rendererWidth, boundarySize),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
    world.createEntity(
      new TransformComponent(0, rendererHeight, rendererWidth, boundarySize),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
    world.createEntity(
      new TransformComponent(-boundarySize, 0, boundarySize, rendererHeight),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
    world.createEntity(
      new TransformComponent(rendererWidth, 0, boundarySize, rendererHeight),
      ColliderComponent.aabb().setTags(Constants.Tags.BOUNDARY)
    );
  }

  private static void addPlayer(World world) {
    world.createEntity(
      new DynamicBodyComponent(),
      new TransformComponent(100, 100, 1, 1),
      new LayerComponent(Constants.Layers.OBJECTS, 2),
      new BlendModeComponent(BlendMode.MASK),
      new LightComponent(50, new Color(200, 255, 255, 255)).setOffset(2.5f, 4),
      new SpriteComponent(Constants.Assets.Sprites.ID, Constants.Assets.Sprites.PLAYER),
      ColliderComponent.aabb(3, 5).setTags(Constants.Tags.PLAYER)
    ).setName(Constants.Names.PLAYER);
  }

  private static void addPortal(World world, float x, float y) {
    ParticleEmitterComponent portalParticleEmitter = new ParticleEmitterComponent();

    // TODO test these settings out later
    portalParticleEmitter.setParticleBlendMode(BlendMode.DISSOLVE);
    portalParticleEmitter.setParticleColor(new Color(220, 220, 220));
    portalParticleEmitter.setParticleSizeBounds(3, 5);
    portalParticleEmitter.setParticleLifeBounds(3, 5);
    portalParticleEmitter.setParticleVelocityBounds(new Vector2D(-5f, -5f), new Vector2D(5f, 5f));
    portalParticleEmitter.setParticleEmissionDelay(0.1f);
    portalParticleEmitter.setParticlesPerEmit(5);

    world.createEntity(
      new PortalComponent(),
      new TransformComponent(x, y, 20),
      ColliderComponent.circle().setSensor(true).setTags(Constants.Tags.PORTAL),
      new CircleRendererComponent(Color.YELLOW, true), // TODO temp until particle layer works
      new LayerComponent(Constants.Layers.OBJECTS),
      portalParticleEmitter
    );
  }
}
