package technology.sola.engine.rememory.components;

import technology.sola.ecs.Component;

public record EnemyComponent(
  technology.sola.engine.rememory.components.EnemyComponent.EnemyType type) implements Component {

  public enum EnemyType {
    CREEPER,
    SPOOKER,
  }
}
