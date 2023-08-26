package technology.sola.javafx.rememory;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.rememory.ReMemorySola;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new JavaFxSolaPlatform();
    Sola sola = new ReMemorySola();

    solaPlatform.play(sola);
  }
}
