package technology.sola.browser;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.rememory.ReMemorySola;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

public class BrowserMain {
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new BrowserSolaPlatform();
    Sola sola = new ReMemorySola();

    solaPlatform.play(sola);
  }
}
