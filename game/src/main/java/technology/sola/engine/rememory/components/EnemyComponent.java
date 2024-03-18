package technology.sola.engine.rememory.components;

import technology.sola.ecs.Component;
import technology.sola.engine.rememory.RandomUtils;
import technology.sola.math.linear.Vector2D;

public class EnemyComponent implements Component {
  private final EnemyType type;
  private Vector2D roamingLocation;

  public EnemyComponent(EnemyType type) {
    this.type = type;
  }

  public EnemyType type() {
    return type;
  }

  public Vector2D getRoamingLocation() {
    return roamingLocation;
  }

  public void newRoamingLocationIfDestinationReached(Vector2D translate, int rendererWidth, int rendererHeight) {
    if (roamingLocation == null || translate.subtract(roamingLocation).magnitudeSq() < 10) {
      roamingLocation = new Vector2D(
        RandomUtils.randomRange(10, rendererWidth - 10),
        RandomUtils.randomRange(10, rendererHeight - 10)
      );
    }
  }

  public enum EnemyType {
    CREEPER,
    SPOOKER,
  }
}
