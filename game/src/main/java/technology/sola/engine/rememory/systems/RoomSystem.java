package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.rememory.attributes.ReMemoryMaker;
import technology.sola.engine.rememory.components.PortalComponent;
import technology.sola.engine.rememory.events.ForgetEverythingEvent;
import technology.sola.engine.rememory.rooms.CozyRoomWorld;
import technology.sola.engine.rememory.rooms.ForestRoomWorld;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.rememory.events.ChangeRoomEvent;
import technology.sola.engine.rememory.rooms.RoomWorld;

import java.util.HashMap;
import java.util.Map;

public class RoomSystem extends EcsSystem {
  private final Renderer renderer;
  private final SolaEcs solaEcs;
  private final ReMemoryMaker reMemoryMaker;
  private final Map<String, RoomWorld> worldMap = new HashMap<>();
  private String currentRoomId;
  private String nextRoomId = "";
  private int idCounter = 0;

  public RoomSystem(EventHub eventHub, Renderer renderer, SolaEcs solaEcs, ReMemoryMaker reMemoryMaker) {
    this.renderer = renderer;
    this.solaEcs = solaEcs;
    this.reMemoryMaker = reMemoryMaker;

    eventHub.add(ChangeRoomEvent.class, event -> {
      if (event.portalComponent().getRoomId() == null) {
        nextRoomId = nextId();
        event.portalComponent().setRoomId(nextRoomId);
      } else {
        nextRoomId = event.portalComponent().getRoomId();
      }

      setActive(true);
    });

    eventHub.add(ForgetEverythingEvent.class, event -> {
      currentRoomId = null;
      idCounter = 0;
      nextRoomId = "";
      worldMap.clear();
      setActive(true);
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    RoomWorld nextRoom = worldMap.get(nextRoomId);

    if (nextRoom == null) {
      if (idCounter == 0) {
        nextRoomId = nextId();
        nextRoom = new ForestRoomWorld(null, renderer.getWidth(), renderer.getHeight());
      } else {
        nextRoom = new CozyRoomWorld(currentRoomId, renderer.getWidth(), renderer.getHeight(), reMemoryMaker);
      }

      currentRoomId = nextRoomId;
      worldMap.put(currentRoomId, nextRoom);
    } else {
      currentRoomId = nextRoomId;
      nextRoom = worldMap.get(nextRoomId);

      // reset room
      nextRoom.findEntitiesWithComponents(ParticleEmitterComponent.class).forEach(Entity::destroy);
      nextRoom.createView().of(PortalComponent.class).getEntries()
        .forEach(entry -> {
          entry.c1().resetActivation();
          entry.entity().removeComponent(ParticleEmitterComponent.class);
          entry.entity().getComponent(CircleRendererComponent.class).setColor(new Color(177, 156, 217));
        });
    }

    solaEcs.setWorld(nextRoom);
    nextRoomId = null;

    setActive(false);
  }

  @Override
  public int getOrder() {
    return -999;
  }

  private String nextId() {
    return "" + idCounter++;
  }
}
