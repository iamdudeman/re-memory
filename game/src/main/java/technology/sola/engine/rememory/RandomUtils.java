package technology.sola.engine.rememory;

import java.util.Random;

public class RandomUtils {
  private static final Random random = new Random();

  public static int roll100() {
    return rollN(100);
  }

  public static int rollN(int n) {
    return random.nextInt(n) + 1;
  }

  public static float quickRandomDoubleClamp(float min, float max, float minExclude, float maxExclude) {
    float value = random.nextFloat(min, max);

    if (minExclude < value && value < maxExclude) {
      if (maxExclude - value > value - minExclude) {
        return random.nextFloat(maxExclude, max);
      } else if (min < minExclude) {
        return random.nextFloat(min, minExclude);
      } else {
        return quickRandomDoubleClamp(min, max, minExclude, maxExclude);
      }
    }

    return value;
  }
}
