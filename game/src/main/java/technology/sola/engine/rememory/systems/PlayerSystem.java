package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.rememory.Constants;
import technology.sola.math.linear.Vector2D;

public class PlayerSystem extends EcsSystem {
  private final KeyboardInput keyboardInput;

  public PlayerSystem(KeyboardInput keyboardInput) {
    this.keyboardInput = keyboardInput;
  }

  @Override
  public void update(World world, float deltaTime) {
    final int speed = 50;
    Entity playerEntity = world.findEntityByName(Constants.Names.PLAYER);
    DynamicBodyComponent dynamicBodyComponent = playerEntity.getComponent(DynamicBodyComponent.class);

    dynamicBodyComponent.setVelocity(new Vector2D(0, 0));

    if (keyboardInput.isKeyHeld(Key.W)) {
      dynamicBodyComponent.setVelocity(new Vector2D(0, -speed));
    }
    if (keyboardInput.isKeyHeld(Key.S)) {
      dynamicBodyComponent.setVelocity(new Vector2D(0, speed));
    }
    if (keyboardInput.isKeyHeld(Key.A)) {
      dynamicBodyComponent.setVelocity(new Vector2D(-speed, dynamicBodyComponent.getVelocity().y()));
    }
    if (keyboardInput.isKeyHeld(Key.D)) {
      dynamicBodyComponent.setVelocity(new Vector2D(speed, dynamicBodyComponent.getVelocity().y()));
    }
  }
}
