package technology.sola.engine.rememory.gui;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.input.Key;

public class DiaryGui {
  private final static String[] pages = {
    "  I don't know what's been going on recently. I've been feeling so confused about what I'm doing and forgetful of things I need to do or have been doing. Do I need more sleep? Do I need to change my diet? Is this just what getting older is like? Until I figure out what's happening, I'm going to start writing things down in this journal to make sure I remember the things I need to.",
    "  This is worse than I could have possibly imagined. Just got off the phone with my doctor. He gave me the grim diagnosis. Alzheimer's. I should have known. The forgetfulness and confusion have only gotten worse. It feels like I get lost in my own home sometimes. Sometimes it takes a while to remember the names of my own children. I feel so scared and alone.",
    "  Today I visited the old lighthouse my grandpa operated when I was a kid. It's the first time in a long time that I've felt any peace. Memories came flooding back. Memories of sitting on his lap as he'd tell me stories or sing old hymns. Like a light shining in darkness, this is the only place I can go to see clearly anymore.",
    "  It's gotten so much worse. I can't think. I can't follow conversations. I can't remember my own children. I need people to guide me around my own home. The only time I can do anything is when I'm here at the lighthouse. It feels like my own brain wants to destroy me. I can't do anything right anymore because I've forgotten how. My loved ones are being taken from me by my own mind. I feel so hopeless. I'm so alone. God help me...",
    // TODO real text
    "  Sit amet commodo nulla facilisi nullam vehicula ipsum a. Vitae purus faucibus ornare suspendisse sed nisi lacus sed. Tellus mauris a diam maecenas sed. Aliquam ut porttitor leo a diam sollicitudin tempor id. Ut morbi tincidunt augue interdum velit euismod in pellentesque massa. Turpis massa sed elementum tempus. Cras tincidunt lobortis feugiat vivamus at. Mus mauris vitae ultricies leo integer malesuada nunc vel risus. Amet consectetur adipiscing elit ut aliquam. Vitae semper quis lectus nulla at volutpat diam ut. Libero id faucibus nisl tincidunt eget nullam non nisi. Vel elit scelerisque mauris pellentesque. Ac placerat vestibulum lectus mauris ultrices eros in cursus turpis. Egestas quis ipsum suspendisse ultrices gravida dictum. Libero enim sed faucibus turpis in eu mi bibendum. Dolor magna eget est lorem ipsum dolor sit amet. Nam aliquam sem et tortor consequat id porta. Nec ullamcorper sit amet risus. Commodo elit at imperdiet dui accumsan sit amet nulla",
    "~~~~~Thank you for playing~~~~~ Music-@Eliteomnomnivore Code-@iamdudeman Art-@iamdudeman Art-@Eliteomnomnivore Art-@denise Story-@Lvl30Caterpie",
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
      if (keyEvent.getKeyCode() == Key.RIGHT.getCode() || keyEvent.getKeyCode() == Key.SPACE.getCode()) {
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
