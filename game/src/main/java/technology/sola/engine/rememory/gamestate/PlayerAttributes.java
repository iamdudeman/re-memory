package technology.sola.engine.rememory.gamestate;

public class PlayerAttributes {
  private static String name = "???";
  private static int speed = 50;
  private static int batteryCount = 3;
  private static float currentBatteryLife = 100f;
  private static float batteryDecreaseRate = 0.1f;
  private static float flashlightStrength = 50;

  public static void forgetWhoIAm() {
    speed = 50;
  }

  public static void forgetWhatIHave() {
    batteryCount = 3;
  }

  public static void forgetWhereIveBeen() {

  }
}
