package technology.sola.engine.rememory.components;

import technology.sola.ecs.Component;

public class PortalComponent implements Component {
  private boolean isActive;
  private float inactiveTicks;
  private static final float ACTIVATION_DELAY = 5;

  public PortalComponent(boolean delayActivation) {
    if (!delayActivation) {
      inactiveTicks = ACTIVATION_DELAY;
    }
  }

  public boolean isActive() {
    return isActive;
  }

  public void tickInactive(float delta) {
    inactiveTicks += delta;
  }

  public boolean canBeActivated() {
    return !isActive && inactiveTicks >= ACTIVATION_DELAY;
  }

  public void activate() {
    isActive = true;
  }
}
