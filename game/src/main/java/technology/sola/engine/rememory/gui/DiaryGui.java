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
    "  I experience the memories of a stranger every day. Each day the same process of \"re-memory\" as I struggle to find and read through this diary. But even so, even if the \"me\" tomorrow is once again a stranger; even if the rest of my days are just re-memories of the past; I will trust in the Light that pierces even this growing darkness.                                                                                           John 1:5",
    "~~~~~Thank you for playing~~~~~ Music-@Eliteomnomnivore Code-@iamdudeman      Art-@Denise      Art-@iamdudeman Art-@Eliteomnomnivore Story-@Lvl30Caterpie Story-@iamdudeman",
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
