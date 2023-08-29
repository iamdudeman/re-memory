package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.rememory.Constants;
import technology.sola.engine.rememory.components.PortalComponent;
import technology.sola.engine.rememory.events.ChangeRoomEvent;
import technology.sola.engine.rememory.events.ForgetWhereEvent;
import technology.sola.engine.rememory.rooms.RoomWorld;
import technology.sola.math.linear.Vector2D;

public class PortalSystem extends EcsSystem {
  private boolean clearPortalIds = false;
  private Vector2D addPortalLocation = null;

  public PortalSystem(EventHub eventHub) {
    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.getComponent(ColliderComponent.class).hasTag(Constants.Tags.PORTAL),
        (player, portal) -> {
          PortalComponent portalComponent = portal.getComponent(PortalComponent.class);

          if (portalComponent.isActive()) {
            eventHub.emit(new ChangeRoomEvent(portalComponent));
          }
        }
      );
    });

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> Constants.Names.PLAYER.equals(entity.getName()),
        entity -> entity.getComponent(ColliderComponent.class).hasTag(Constants.Tags.LAPIS),
        (player, lapis) -> {
          lapis.destroy();

          addPortalLocation = player.getComponent(TransformComponent.class).getTranslate();
        }
      );
    });

    eventHub.add(ForgetWhereEvent.class, event -> {
      clearPortalIds = true;
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    if (addPortalLocation != null) {
      ((RoomWorld) world).addPortal(addPortalLocation.x(), addPortalLocation.y());

      addPortalLocation = null;
    }

    world.createView().of(PortalComponent.class)
      .getEntries()
      .forEach(entry -> {
        if (clearPortalIds) {
          entry.c1().setRoomId(null);
        }

        if (entry.c1().canBeActivated()) {
          ParticleEmitterComponent portalParticleEmitter = new ParticleEmitterComponent();

          // TODO test these settings out later when particle emitter layer bug fixed
          portalParticleEmitter.setParticleBlendMode(BlendMode.MULTIPLY);
          portalParticleEmitter.setParticleColor(new Color(120, 177, 156, 217));
          portalParticleEmitter.setParticleSizeBounds(1, 3);
          portalParticleEmitter.setParticleLifeBounds(1, 3);
          portalParticleEmitter.setParticleVelocityBounds(new Vector2D(-4f, -5f), new Vector2D(4f, 0));
          portalParticleEmitter.setParticleEmissionDelay(0.25f);
          portalParticleEmitter.setParticlesPerEmit(5);

          world.createEntity(
            new TransformComponent(3, 5, entry.entity()),
            new LayerComponent(Constants.Layers.OBJECTS, 2),
            portalParticleEmitter
          );

//          entry.entity().addComponent(portalParticleEmitter);
//          entry.entity().removeComponent(CircleRendererComponent.class); // todo do this when particle emitter layer bug fixed
          entry.entity().getComponent(CircleRendererComponent.class).setColor(new Color(177, 156, 217));
          entry.c1().activate();
        } else {
          entry.c1().tickInactive(deltaTime);
        }
      });

    clearPortalIds = false;
  }
}
