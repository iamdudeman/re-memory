package technology.sola.engine.rememory.systems;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.rememory.RoomBuilders;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.rememory.events.NewRoomEvent;

public class ChangeRoomSystem extends EcsSystem {
  private final Renderer renderer;
  private final SolaEcs solaEcs;

  public ChangeRoomSystem(EventHub eventHub, Renderer renderer, SolaEcs solaEcs) {
    this.renderer = renderer;
    this.solaEcs = solaEcs;

    setActive(false);

    eventHub.add(NewRoomEvent.class, event -> {
      setActive(true);
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    solaEcs.setWorld(
      RoomBuilders.buildCozy(renderer.getWidth(), renderer.getHeight())
    );

    setActive(false);
  }

  @Override
  public int getOrder() {
    return 999;
  }
}
