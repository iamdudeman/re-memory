package technology.sola.engine.rememory;

import technology.sola.engine.physics.component.ColliderComponent;

public final class Constants {
  public final static class Assets {
    public final static class AudioClips {
      public static final String QUACK = "quack";
    }

    public final static class CozySprites {
      public static final String ID = "cozy";

      public static final String FLOOR = "floor";
      public static final String BACK_WALL_TOP = "back_wall_top";
      public static final String BACK_WALL_BOTTOM = "back_wall_bottom";
    }

    public final static class AcidRainSprites {
      public static final String ID = "acid_rain";

      public static final String DUCK = "duck";
      public static final String DONUT = "donut";
    }

    public final static class Sprites {
      public static final String ID = "sprites";

      public static final String PLAYER = "player";
      public static final String TREE = "tree";
      public static final String TORCH = "torch";
      public static final String GRASS_1 = "grass_1";
      public static final String GRASS_2 = "grass_2";
      public static final String GRASS_3 = "grass_3";
      public static final String TABLE = "table";
      public static final String LAPIS = "lapis";
      public static final String PAGE = "page";
    }
  }

  public final static class Layers {
    public static final String DECORATION = "decoration";
    public static final String OBJECTS = "objects";
  }

  public final static class Names {
    public static final String PLAYER = "player";
  }

  public enum Tags implements ColliderComponent.ColliderTag {
    BOUNDARY,
    PLAYER,
    PORTAL,
    LAPIS,
    DUCK,
  }
}
