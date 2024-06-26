package technology.sola.engine.rememory;

import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.collider.ColliderTag;

public final class Constants {
  public final static class Assets {
    public final static class AudioClips {
      public static final String QUACK = "quack";
      public static final String BELL = "bell";
      public static final String FORGET = "forget";
    }

    public final static class Gui {
      public static final String CONTROLS = "controls";
      public static final String IN_GAME = "in-game";
      public static final String DIARY = "DIARY";
    }

    public final static class Font {
      public static final String MONO_10 = "monospaced_NORMAL_10";
    }

    public final static class CozySprites {
      public static final String ID = "cozy";

      public static final String FLOOR = "floor";
      public static final String BACK_WALL_TOP = "back_wall_top";
      public static final String BACK_WALL_BOTTOM = "back_wall_bottom";
    }

    public final static class BasementSprites {
      public static final String ID = "basement";

      public static final String FLOOR = "floor";
      public static final String BACK_WALL = "back_wall";
      public static final String LANTERN = "lantern";
    }

    public final static class AcidRainSprites {
      public static final String ID = "acid_rain";

      public static final String DUCK = "duck";
    }

    public final static class LibrarySprites {
      public static final String ID = "library";

      public static final String SHELF_1 = "shelf1";
      public static final String SHELF_2 = "shelf2";
      public static final String TABLE_SET = "table_set";
    }

    public final static class Sprites {
      public static final String ID = "sprites";

      public static final String PLAYER = "player";
      public static final String TREE = "tree";
      public static final String BERRY_BUSH = "berry_bush";
      public static final String TORCH = "torch";
      public static final String GRASS_1 = "grass_1";
      public static final String GRASS_2 = "grass_2";
      public static final String GRASS_3 = "grass_3";
      public static final String TABLE = "table";
      public static final String LAPIS = "lapis";
      public static final String PAGE = "page";
      public static final String LIGHTHOUSE = "lighthouse";
    }
  }

  public final static class Layers {
    public static final String DECORATION = "decoration";
    public static final String OBJECTS = "objects";
  }

  public final static class Names {
    public static final String PLAYER = "player";
  }

  public enum Tags implements ColliderTag {
    BOUNDARY,
    PLAYER,
    PORTAL,
    LAPIS,
    DUCK,
  }
}
