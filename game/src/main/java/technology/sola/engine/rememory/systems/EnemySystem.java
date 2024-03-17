package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.components.EnemyComponent;
import technology.sola.engine.rememory.events.ChangeRoomEvent;
import technology.sola.engine.rememory.events.ForgetPagesEvent;
import technology.sola.math.linear.Vector2D;

public class EnemySystem extends EcsSystem {
  private final PlayerAttributeContainer playerAttributeContainer;
  private final int rendererWidth;
  private final int rendererHeight;
  private float enemyMoveDelay = 0;

  public EnemySystem(EventHub eventHub, PlayerAttributeContainer playerAttributeContainer, AssetLoader<AudioClip> audioClipAssetLoader, Renderer renderer) {
    this.playerAttributeContainer = playerAttributeContainer;
    this.rendererWidth = renderer.getWidth();
    this.rendererHeight = renderer.getHeight();

    eventHub.add(CollisionEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.hasComponent(EnemyComponent.class),
        (player, enemy) -> {
          audioClipAssetLoader.get(Constants.Assets.AudioClips.BELL).executeIfLoaded(AudioClip::play);
          audioClipAssetLoader.get(Constants.Assets.AudioClips.FORGET).executeIfLoaded(AudioClip::play);
          enemy.destroy();
          eventHub.emit(new ForgetPagesEvent());
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

        float initialDetectionRange = enemyType == EnemyComponent.EnemyType.CREEPER ? 140 : 70;
        float detectionRange = initialDetectionRange - initialDetectionRange * (playerAttributeContainer.getStealth() / (2f * PlayerAttributeContainer.STAT_CAP));

        if (distanceFromPlayer < detectionRange) {
          float speed = enemyType == EnemyComponent.EnemyType.CREEPER ? RandomUtils.randomRange(22, 32) : RandomUtils.randomRange(50, 65);

          dynamicBodyComponent.setVelocity(playerTranslate.subtract(enemyTranslate).normalize().scalar(speed));
        } else {
          var enemyComponent = entry.c3();
          float roamingSpeed = enemyType == EnemyComponent.EnemyType.CREEPER ? 20 : 10;

          enemyComponent.newRoamingLocationIfDestinationReached(enemyTranslate, rendererWidth, rendererHeight);
          dynamicBodyComponent.setVelocity(enemyComponent.getRoamingLocation().subtract(enemyTranslate).normalize().scalar(roamingSpeed));
        }
      });
  }
}
