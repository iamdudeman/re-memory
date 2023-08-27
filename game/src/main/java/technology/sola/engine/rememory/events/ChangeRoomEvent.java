package technology.sola.engine.rememory.events;

import technology.sola.engine.event.Event;
import technology.sola.engine.rememory.components.PortalComponent;

public record ChangeRoomEvent(PortalComponent portalComponent) implements Event {
}
