package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.PortalComponent;
import technology.sola.engine.rememory.events.NewRoomEvent;
import technology.sola.math.linear.Vector2D;

public class PortalSystem extends EcsSystem {
  public PortalSystem(EventHub eventHub) {
    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.getComponent(ColliderComponent.class).hasTag(Constants.Tags.PORTAL),
        (player, portal) -> {
          if (portal.getComponent(PortalComponent.class).isActive()) {
            eventHub.emit(new NewRoomEvent(null));
          }
        }
      );
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(PortalComponent.class)
      .getEntries()
      .forEach(entry -> {
        if (entry.c1().canBeActivated()) {
          ParticleEmitterComponent portalParticleEmitter = new ParticleEmitterComponent();

          // TODO test these settings out later when particle emitter layer bug fixed
          portalParticleEmitter.setParticleBlendMode(BlendMode.DISSOLVE);
          portalParticleEmitter.setParticleColor(new Color(220, 220, 220));
          portalParticleEmitter.setParticleSizeBounds(3, 5);
          portalParticleEmitter.setParticleLifeBounds(3, 5);
          portalParticleEmitter.setParticleVelocityBounds(new Vector2D(-5f, -5f), new Vector2D(5f, 5f));
          portalParticleEmitter.setParticleEmissionDelay(0.1f);
          portalParticleEmitter.setParticlesPerEmit(5);

          entry.entity().addComponent(portalParticleEmitter);
//          entry.entity().removeComponent(CircleRendererComponent.class); // todo do this when particle emitter layer bug fixed
          entry.entity().getComponent(CircleRendererComponent.class).setColor(Color.YELLOW);
          entry.c1().activate();
        } else {
          entry.c1().tickInactive(deltaTime);
        }
      });
  }
}
