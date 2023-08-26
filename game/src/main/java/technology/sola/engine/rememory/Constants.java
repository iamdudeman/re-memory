package technology.sola.engine.rememory;

import technology.sola.engine.physics.component.ColliderComponent;

public final class Constants {
  public final static class Assets {
    public final static class CozySprites {
      public static final String ID = "cozy";
      public static final String FLOOR = "floor";
      public static final String BACK_WALL_TOP = "back_wall_top";
      public static final String BACK_WALL_BOTTOM = "back_wall_bottom";
    }

    public final static class Sprites {
      public static final String ID = "sprites";

      public static final String PLAYER = "player";
      public static final String TREE = "tree";
      public static final String TORCH = "torch";
      public static final String GRASS_1 = "grass_1";
      public static final String GRASS_2 = "grass_2";
      public static final String GRASS_3 = "grass_3";
      public static final String BATTERY = "battery";
      public static final String KEY = "key";
      public static final String TABLE_EMPTY = "table_empty";
      public static final String TABLE_PAGE = "table_page";

      private Sprites() {
      }
    }

    private Assets() {
    }
  }

  public final static class Layers {
    public static final String OBJECTS = "objects";
  }

  public final static class Names {
    public static final String PLAYER = "player";

    private Names() {
    }
  }

  public enum Tags implements ColliderComponent.ColliderTag {
    PORTAL,
    PLAYER
  }

  private Constants() {
  }
}
