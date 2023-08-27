package technology.sola.engine.rememory.components;

import technology.sola.ecs.Component;
import technology.sola.engine.rememory.gamestate.ReMemoryPage;

public record PageComponent(ReMemoryPage reMemoryPage) implements Component {
}
