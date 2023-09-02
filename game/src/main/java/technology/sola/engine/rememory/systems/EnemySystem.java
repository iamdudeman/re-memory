package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.components.EnemyComponent;
import technology.sola.engine.rememory.events.ChangeRoomEvent;
import technology.sola.engine.rememory.events.ForgetEverythingEvent;
import technology.sola.math.linear.Vector2D;

public class EnemySystem extends EcsSystem {
  private final PlayerAttributeContainer playerAttributeContainer;
  private float enemyMoveDelay = 0;

  public EnemySystem(EventHub eventHub, PlayerAttributeContainer playerAttributeContainer, AssetLoader<AudioClip> audioClipAssetLoader) {
    this.playerAttributeContainer = playerAttributeContainer;

    eventHub.add(CollisionEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(EnemyComponent.class),
        (player, enemy) -> {
          audioClipAssetLoader.get(Constants.Assets.AudioClips.BELL).executeIfLoaded(AudioClip::play);
          enemy.destroy();
          eventHub.emit(new ForgetEverythingEvent());
        }
      );
    });

    eventHub.add(ChangeRoomEvent.class, event -> {
      enemyMoveDelay = 0;
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    if (enemyMoveDelay < 1.5f) {
      enemyMoveDelay += deltaTime;
      return;
    }

    Entity playerEntity = world.findEntityByName(Constants.Names.PLAYER);
    Vector2D playerTranslate = playerEntity.getComponent(TransformComponent.class).getTranslate();

    world.createView().of(TransformComponent.class, DynamicBodyComponent.class, EnemyComponent.class).getEntries()
      .forEach(entry -> {
        EnemyComponent.EnemyType enemyType = entry.c3().type();
        Vector2D enemyTranslate = entry.c1().getTranslate();
        DynamicBodyComponent dynamicBodyComponent = entry.c2();
        float distanceFromPlayer = playerTranslate.distance(enemyTranslate);

        float speed = enemyType == EnemyComponent.EnemyType.CREEPER ? 20 : 48;
        float initialDetectionRange = enemyType == EnemyComponent.EnemyType.CREEPER ? 300 : 120;
        float detectionRange = Math.max(initialDetectionRange - playerAttributeContainer.getStealth() * 20, 40);

        if (distanceFromPlayer < detectionRange) {
          dynamicBodyComponent.setVelocity(playerTranslate.subtract(enemyTranslate).normalize().scalar(speed));
        } else {
          dynamicBodyComponent.setVelocity(new Vector2D(0, 0));
        }
      });
  }
}
