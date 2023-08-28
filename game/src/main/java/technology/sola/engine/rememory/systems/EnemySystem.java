package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.EnemyComponent;

public class EnemySystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    Entity playerEntity = world.findEntityByName(Constants.Names.PLAYER);

    world.createView().of(TransformComponent.class, EnemyComponent.class).getEntries()
      .forEach(entry -> {
        // todo
      });
  }
}
