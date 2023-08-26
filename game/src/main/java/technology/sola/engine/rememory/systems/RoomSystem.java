package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.rememory.rooms.RoomBuilders;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.rememory.events.ChangeRoomEvent;

import java.util.HashMap;
import java.util.Map;

public class RoomSystem extends EcsSystem {
  private final Renderer renderer;
  private final SolaEcs solaEcs;
  private final Map<String, World> worldMap = new HashMap<>();
  private String nextRoomId;
  private int idCounter = 0;

  public RoomSystem(EventHub eventHub, Renderer renderer, SolaEcs solaEcs) {
    this.renderer = renderer;
    this.solaEcs = solaEcs;

    eventHub.add(ChangeRoomEvent.class, event -> {
      nextRoomId = event.roomId();
      setActive(true);
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    World nextRoom;

    if (nextRoomId == null) {
      if (idCounter == 0) {
        nextRoom = RoomBuilders.buildForest(renderer.getWidth(), renderer.getHeight());
      } else {
        nextRoom = RoomBuilders.buildCozy(renderer.getWidth(), renderer.getHeight());
      }

      worldMap.put(nextId(), nextRoom);
    } else {
      nextRoom = worldMap.get(nextRoomId);
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
