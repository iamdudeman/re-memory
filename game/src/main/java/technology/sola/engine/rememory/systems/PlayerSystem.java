package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.rememory.Constants;

public class PlayerSystem extends EcsSystem {
  private final KeyboardInput keyboardInput;

  public PlayerSystem(KeyboardInput keyboardInput) {
    this.keyboardInput = keyboardInput;
  }

  @Override
  public void update(World world, float deltaTime) {
    final int speed = 2;
    Entity playerEntity = world.findEntityByName(Constants.Names.PLAYER);
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
}
