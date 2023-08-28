package technology.sola.engine.rememory.components;

import technology.sola.ecs.Component;

public record EnemyComponent(
  technology.sola.engine.rememory.components.EnemyComponent.EnemyType type) implements Component {

  public enum EnemyType {
    // todo creeper type with bigger range but slower speed todo spooker type with smaller range but fast speed
    CREEPER,
    SPOOKER,
  }
}
