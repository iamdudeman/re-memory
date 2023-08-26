package technology.sola.engine.rememory.events;

import technology.sola.engine.event.Event;

public record ChangeRoomEvent(String roomId) implements Event {
}
