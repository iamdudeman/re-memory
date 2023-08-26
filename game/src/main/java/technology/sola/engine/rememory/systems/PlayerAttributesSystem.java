package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;

public class PlayerAttributesSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {

  }

  @Override
  public int getOrder() {
    return 99;
  }
}
