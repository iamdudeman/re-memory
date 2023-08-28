package technology.sola.engine.rememory;

import java.util.Random;

public class RandomUtils {
  private static final Random random = new Random();

  public static int roll100() {
    return random.nextInt(100);
  }

  public static float quickRandomDoubleClamp(float min, float max, float minExclude, float maxExclude) {
    float value = random.nextFloat(min, max);

    if (minExclude < value && value < maxExclude) {
      if (maxExclude - value > value - minExclude) {
        return random.nextFloat(maxExclude, max);
      } else {
        return random.nextFloat(min, minExclude);
      }
    }

    return value;
  }
}
