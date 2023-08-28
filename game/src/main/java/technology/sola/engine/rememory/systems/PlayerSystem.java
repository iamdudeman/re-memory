package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.events.ForgetWhereEvent;
import technology.sola.engine.rememory.attributes.PlayerAttributeContainer;
import technology.sola.engine.rememory.events.ForgetWhoEvent;
import technology.sola.engine.rememory.rooms.RoomWorld;
import technology.sola.math.linear.Vector2D;

public class PlayerSystem extends EcsSystem {
  private final float halfRootTwo = (float) (Math.sqrt(2) * 0.5f);
  private final KeyboardInput keyboardInput;
  private final EventHub eventHub;
  private final PlayerAttributeContainer playerAttributeContainer;
  private int lapisCount = 0;

  public PlayerSystem(KeyboardInput keyboardInput, EventHub eventHub, PlayerAttributeContainer playerAttributeContainer) {
    this.keyboardInput = keyboardInput;
    this.eventHub = eventHub;
    this.playerAttributeContainer = playerAttributeContainer;

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.getComponent(ColliderComponent.class).hasTag(Constants.Tags.LAPIS),
        (player, lapis) -> {
          lapisCount++;
          lapis.destroy();
        }
      );
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    Entity playerEntity = world.findEntityByName(Constants.Names.PLAYER);
    DynamicBodyComponent dynamicBodyComponent = playerEntity.getComponent(DynamicBodyComponent.class);
    LightComponent lightComponent = playerEntity.getComponent(LightComponent.class);

    // apply attributes
    final int speed = playerAttributeContainer.getSpeed() * 20;
    lightComponent.setRadius(playerAttributeContainer.getVision() * 20);

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

    if (keyboardInput.isKeyPressed(Key.SPACE) && lapisCount > 0) {
      lapisCount--;
      TransformComponent transformComponent = playerEntity.getComponent(TransformComponent.class);
      ((RoomWorld) world).addPortal(transformComponent.getX(), transformComponent.getY());
    }

    if (keyboardInput.isKeyPressed(Key.ONE)) {
      eventHub.emit(new ForgetWhereEvent());
    }
    if (keyboardInput.isKeyPressed(Key.TWO)) {
      eventHub.emit(new ForgetWhoEvent());
    }

    dynamicBodyComponent.setVelocity(new Vector2D(xSpeed, ySpeed));
  }
}
