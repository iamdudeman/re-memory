package technology.sola.engine.rememory.gui;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.input.Key;

public class DiaryGui {
  // TODO real text
  private final static String[] pages = {
    "  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    "  Eu ultrices vitae auctor eu augue. Mattis aliquam faucibus purus in massa tempor nec feugiat. Sit amet purus gravida quis. Sed turpis tincidunt id aliquet risus feugiat in ante metus. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices. Ultricies mi quis hendrerit dolor magna eget. Donec et odio pellentesque diam. Leo urna molestie at elementum. Ut consequat semper viverra nam libero. Quam vulputate dignissim suspendisse in est. Arcu dictum varius duis at consectetur. At urna condimentum mattis pellentesque id nibh. Nibh nisl condimentum id venenatis.",
    "  Lobortis scelerisque fermentum dui faucibus. Consectetur a erat nam at lectus urna duis convallis convallis. Mattis pellentesque id nibh tortor. Pulvinar sapien et ligula ullamcorper malesuada proin. Dignissim convallis aenean et tortor at risus. Id velit ut tortor pretium viverra suspendisse potenti nullam ac. Lectus quam id leo in vitae turpis. Donec ac odio tempor orci dapibus ultrices in iaculis nunc. Velit sed ullamcorper morbi tincidunt ornare massa eget egestas purus. Amet cursus sit amet dictum sit amet. Vitae semper quis lectus nulla at. Massa placerat duis ultricies lacus sed turpis tincidunt.",
    "  Nibh cras pulvinar mattis nunc sed blandit libero volutpat. Vestibulum lectus mauris ultrices eros in. Dapibus ultrices in iaculis nunc sed augue lacus viverra. Lacus sed turpis tincidunt id aliquet risus feugiat. Phasellus egestas tellus rutrum tellus pellentesque. Mollis aliquam ut porttitor leo a diam sollicitudin tempor id. Neque gravida in fermentum et sollicitudin. Vel pretium lectus quam id leo in vitae turpis. Vivamus arcu felis bibendum ut tristique et egestas quis. Bibendum neque egestas congue quisque egestas diam in arcu cursus. Vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur. Quisque egestas diam in arcu cursus euismod quis viverra nibh",
    "  Sit amet commodo nulla facilisi nullam vehicula ipsum a. Vitae purus faucibus ornare suspendisse sed nisi lacus sed. Tellus mauris a diam maecenas sed. Aliquam ut porttitor leo a diam sollicitudin tempor id. Ut morbi tincidunt augue interdum velit euismod in pellentesque massa. Turpis massa sed elementum tempus. Cras tincidunt lobortis feugiat vivamus at. Mus mauris vitae ultricies leo integer malesuada nunc vel risus. Amet consectetur adipiscing elit ut aliquam. Vitae semper quis lectus nulla at volutpat diam ut. Libero id faucibus nisl tincidunt eget nullam non nisi. Vel elit scelerisque mauris pellentesque. Ac placerat vestibulum lectus mauris ultrices eros in cursus turpis. Egestas quis ipsum suspendisse ultrices gravida dictum. Libero enim sed faucibus turpis in eu mi bibendum. Dolor magna eget est lorem ipsum dolor sit amet. Nam aliquam sem et tortor consequat id porta. Nec ullamcorper sit amet risus. Commodo elit at imperdiet dui accumsan sit amet nulla",
    "~~~~~Thank you for playing~~~~~ Music-@Eliteomnomnivore Code-@iamdudeman Art-@iamdudeman",
  };

  private static int pageIndex = 0;

  static GuiElement<?> build(SolaGuiDocument document) {
    var textElement = document.createElement(
      TextGuiElement::new,
      p -> p.setText(pages[0]).setWidth(196).setHeight(230).setBorderColor(Color.LIGHT_GRAY).setBackgroundColor(Color.WHITE).padding.set(0, 3, 2)
    );
    final Runnable changePage = () -> {
      if (pageIndex > pages.length - 1) {
        pageIndex = pages.length - 1;
      } else if (pageIndex < 0) {
        pageIndex = 0;
      }

      textElement.properties().setText(pages[pageIndex]);
    };

    textElement.properties().setFocusable(true);
    textElement.setOnKeyPressCallback(keyEvent -> {
      if (keyEvent.getKeyCode() == Key.RIGHT.getCode() || keyEvent.getKeyCode() == Key.ENTER.getCode() || keyEvent.getKeyCode() == Key.SPACE.getCode()) {
        pageIndex++;
        changePage.run();
      } else if (keyEvent.getKeyCode() == Key.LEFT.getCode()) {
        pageIndex--;
        changePage.run();
      }
    });
    textElement.setOnMouseDownCallback(mouseEvent -> {
      pageIndex++;
      changePage.run();
    });

    return textElement;
  }
}
