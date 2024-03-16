package technology.sola.engine.rememory.gui;

import technology.sola.engine.rememory.PlayerAttributeContainer;

public class DialogUtil {
  public static final String[] pages = {
    """
I stand in darkness of the mind
I can't recall what came behind
All memory is but a haze
Of fear and torment, like a maze
    """,
    """
I search and seek to realize
To bring the past back to my eyes
But whispers in the deathly dark
Bid me to cease, to disembark.
    """,
    """
Always this lurker in the dark
Enjoins me with entreaties stark
Bids me to sink in the abyss
Of blessed sleep of blissfulness
    """,
    """
My soul doth drift upon this sea
Whose blackness doth consumeth me
This all surrounding shadow soars
But hark! A light upon the shores!
    """,
    """
A lighthouse beacon in the mist
Stands like a bulwark to assist
My weary soul and darkened mind
To guide me to the truth so kind
    """,
    """
Though sail I in forgetfulness
There is no greater bliss than this
That though my past's in shadow bands
Christ holds it in eternal hands
    """,
    """
~~~~~~Thank you for playing~~~~~~
@Lvl30Caterpie      Poem
@Eliteomnomnivore   Music & Art
@Denise             Art
@iamdudeman         Code & Art
    """,
  };

  public static String getNextEnemyResponseText(PlayerAttributeContainer playerAttributeContainer) {
    int pagesCollected = playerAttributeContainer.getPagesCollectedCount();
    int tries = playerAttributeContainer.getTries();

    return switch (pagesCollected) {
      case 0 -> {
        if (tries == 1) {
          yield "Hey, props to you for trying to remember, but it only gets harder from here.";
        } else if (tries == 2) {
          yield "See, I told you didn't I? Finding four more pages isn't worth the effort!";
        } else {
          yield "You're not making any progress. You've earned your rest already just give it up.";
        }
      }
      case 1 -> {
        if (tries == 1) {
          yield "I wasn't lying to you ya know. It's gonna get rougher...";
        } else if (tries == 2) {
          yield "Congrats on another step forward. We should go back to the forest and celebrate!";
        } else {
          yield "You haven't even made it halfway yet. Why do you keep trying to remember this?";
        }
      }
      case 2 -> {
        if (tries == 1) {
          yield "Ahh you already found the lighthouse. We can't have you getting ahead of yourself now.";
        } else if (tries == 2) {
          yield "I've always been with you. Seeing this lighthouse again changes nothing.";
        } else {
          yield "Took you long enough to get back here. Was this lighthouse really worth it?";
        }
      }
      case 3 -> {
        if (tries == 1) {
          yield "Remembering will just bring you pain. Stay with me instead!";
        } else if (tries == 2) {
          yield "Would you leave me behind after all that I have done for you?";
        } else {
          yield "After so many failures you've done well to make it this far. Let's go back and rest now for awhile.";
        }
      }
      case 4 -> "You don't need to remember! It's not to late to turn back...";
      case 5 -> "The final message before showing whole diary";
      default -> "If you're reading this, then the game has a bug!";
    };
  }
}
