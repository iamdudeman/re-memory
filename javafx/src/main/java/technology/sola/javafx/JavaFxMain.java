package technology.sola.javafx;

import technology.sola.engine.core.Sola;
import technology.sola.engine.rememory.ReMemorySola;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    JavaFxSolaPlatform solaPlatform = new JavaFxSolaPlatform();
    Sola sola = new ReMemorySola();

    solaPlatform.setWindowSize(768, 720);

    solaPlatform.play(sola);
  }
}
