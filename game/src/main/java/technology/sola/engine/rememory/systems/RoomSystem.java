package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.rememory.PlayerAttributeContainer;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.engine.rememory.events.ForgetEverythingEvent;
import technology.sola.engine.rememory.rooms.*;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.rememory.events.ChangeRoomEvent;

public class RoomSystem extends EcsSystem {
  private final EventHub eventHub;
  private final Renderer renderer;
  private final SolaEcs solaEcs;
  private final PlayerAttributeContainer playerAttributeContainer;
  private boolean isStart = true;

  public RoomSystem(EventHub eventHub, Renderer renderer, SolaEcs solaEcs, PlayerAttributeContainer playerAttributeContainer) {
    this.eventHub = eventHub;
    this.renderer = renderer;
    this.solaEcs = solaEcs;
    this.playerAttributeContainer = playerAttributeContainer;

    eventHub.add(ChangeRoomEvent.class, event -> {
      setActive(true);
    });

    eventHub.add(ForgetEverythingEvent.class, event -> {
      isStart = true;
      setActive(true);
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    RoomWorld nextRoom;

    if (isStart) {
      nextRoom = new StartingRoomWorld(renderer.getWidth(), renderer.getHeight());
    } else if (playerAttributeContainer.getPagesCollectedCount() == 0) {
      nextRoom = new InitialRoomWorld(renderer.getWidth(), renderer.getHeight(), eventHub);
    } else {
      if (playerAttributeContainer.getPagesCollectedCount() > 3) {
        nextRoom = new BasementRoomWorld(renderer.getWidth(), renderer.getHeight(), playerAttributeContainer);
      } else if (playerAttributeContainer.getPagesCollectedCount() > 2) {
        nextRoom = RandomUtils.roll100() < 75 ?
          new BasementRoomWorld(renderer.getWidth(), renderer.getHeight(), playerAttributeContainer) :
          new CozyRoomWorld(renderer.getWidth(), renderer.getHeight(), playerAttributeContainer);
      } else {
        nextRoom = new CozyRoomWorld(renderer.getWidth(), renderer.getHeight(), playerAttributeContainer);
      }
    }

    solaEcs.setWorld(nextRoom);

    isStart = false;
    setActive(false);
  }

  @Override
  public int getOrder() {
    return -999;
  }
}
