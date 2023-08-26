package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.events.NewRoomEvent;
import technology.sola.math.linear.Vector2D;

public class PlayerSystem extends EcsSystem {
  private final KeyboardInput keyboardInput;
  private final float halfRootTwo = (float) (Math.sqrt(2) * 0.5f);

  public PlayerSystem(KeyboardInput keyboardInput, EventHub eventHub) {
    this.keyboardInput = keyboardInput;

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.getComponent(ColliderComponent.class).hasTag(Constants.Tags.PORTAL),
        (one, two) -> {
          eventHub.emit(new NewRoomEvent(null));
        }
      );
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    final int speed = 50;
    Entity playerEntity = world.findEntityByName(Constants.Names.PLAYER);
    DynamicBodyComponent dynamicBodyComponent = playerEntity.getComponent(DynamicBodyComponent.class);

    float xSpeed = 0;
    float ySpeed = 0;

    if (keyboardInput.isKeyHeld(Key.W)) {
      ySpeed = -speed;
    }
    if (keyboardInput.isKeyHeld(Key.S)) {
      ySpeed = speed;
    }
    if (keyboardInput.isKeyHeld(Key.A)) {
      if (ySpeed == 0) {
        xSpeed = -speed;
      } else {
        xSpeed = -speed * halfRootTwo;
        ySpeed *= halfRootTwo;
      }
    }
    if (keyboardInput.isKeyHeld(Key.D)) {
      if (ySpeed == 0) {
        xSpeed = speed;
      } else {
        xSpeed = speed * halfRootTwo;
        ySpeed *= halfRootTwo;
      }
    }

    dynamicBodyComponent.setVelocity(new Vector2D(xSpeed, ySpeed));

  }
}
