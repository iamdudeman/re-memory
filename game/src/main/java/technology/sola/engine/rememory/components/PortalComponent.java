package technology.sola.engine.rememory.components;

import technology.sola.ecs.Component;

public class PortalComponent implements Component {
  private boolean isActive;
  private float inactiveTicks;
  private static final float ACTIVATION_DELAY = 5;
  private String roomId;

  public PortalComponent(String roomId, boolean delayActivation) {
    this.roomId = roomId;

    if (!delayActivation) {
      inactiveTicks = ACTIVATION_DELAY;
    }
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
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

  public void resetActivation() {
    isActive = false;
    inactiveTicks = 0;
  }
}
