package technology.sola.engine.rememory.events;

import technology.sola.engine.event.Event;
import technology.sola.engine.rememory.attributes.ReMemoryPage;

public record PageAcceptedEvent(ReMemoryPage reMemoryPage) implements Event {
}
