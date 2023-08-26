package technology.sola.engine.rememory;

import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;

public class ReMemorySola extends SolaWithDefaults {
  public ReMemorySola() {
    super(SolaConfiguration.build("re;memory", 256, 240).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().useLighting(new Color(10, 10, 10)).useBackgroundColor(Color.WHITE);

    assetLoaderProvider.get(SpriteSheet.class)
      .addAssetMapping("main", "assets/forest_spritesheet.json");

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    solaEcs.addSystems(new PlayerSystem());
    solaEcs.setWorld(buildWorld());
  }

  private World buildWorld() {
    World world = new World(100);

    world.createEntity()
      .addComponent(new PlayerComponent())
      .addComponent(new TransformComponent(100, 100, 1, 1))
      .addComponent(new SpriteComponent("main", "player"))
      .addComponent(new BlendModeComponent(BlendMode.MASK))
      .addComponent(ColliderComponent.aabb(16, 16))
      .addComponent(new LightComponent(50, new Color(200, 255, 255, 255)).setOffset(2.5f, 4))
      .setName("player");


    return world;
  }

  private record PlayerComponent() implements Component {
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      final int speed = 2;
      Entity playerEntity = world.findEntityByName("player");
      TransformComponent transformComponent = playerEntity.getComponent(TransformComponent.class);

      if (keyboardInput.isKeyHeld(Key.W)) {
        transformComponent.setY(transformComponent.getY() - speed);
      }
      if (keyboardInput.isKeyHeld(Key.S)) {
        transformComponent.setY(transformComponent.getY() + speed);
      }
      if (keyboardInput.isKeyHeld(Key.A)) {
        transformComponent.setX(transformComponent.getX() - speed);
      }
      if (keyboardInput.isKeyHeld(Key.D)) {
        transformComponent.setX(transformComponent.getX() + speed);
      }
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
