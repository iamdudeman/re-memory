package technology.sola.engine.rememory.events;

import technology.sola.engine.event.Event;

public class NewRoomEvent implements Event {
  private final String previousRoomId;

  public NewRoomEvent(String previousRoomId) {
    this.previousRoomId = previousRoomId;
  }

  public String getPreviousRoomId() {
    return previousRoomId;
  }
}
