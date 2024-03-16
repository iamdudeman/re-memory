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
@Lvl30Caterpie               Poem
@Eliteomnomnivore     Music & Art
@Denise                       Art
@iamdudeman            Code & Art
    """,
  };

  public static String getNextEnemyResponseText(PlayerAttributeContainer playerAttributeContainer) {
    int pagesCollected = playerAttributeContainer.getPagesCollectedCount();
    int maxPagesCollected = playerAttributeContainer.getMaxPagesCollectedCount();
    int tries = playerAttributeContainer.getTries();

    return switch (pagesCollected) {
      case 0 -> {
        if (tries == 1) {
          yield "Hey, props to you for trying to remember, but the pain only gets worse. It's not worth the effort!";
        } else if (tries == 2) {
          yield "See, I told you didn't I? Nothing on these pages will help you escape this darkness anyway!";
        } else {
          if (tries == 3) {
            yield "You haven't given up yet?";
          }

          if (maxPagesCollected < 3) {
            yield "You're not making any progress. You've earned your rest already just give it up... ";
          }

          yield "";
        }
      }

      case 1 -> {
        if (tries == 1) {
          yield "I wasn't lying to you ya know. Can your heart handle it I wonder...";
        } else if (tries == 2) {
          yield "Congrats on another step forward. We should go back to the forest to celebrate!";
        } else {
          yield "Why do you keep trying to remember?";
        }
      }

      case 2 -> {
        if (tries == 1 && playerAttributeContainer.getSpeed() > 3) {
          yield "Whoa! You're really speeding through this. Do you really want to leave me that badly?";
        }

        if (tries < 4) {
          yield "Growing weary yet?";
        }

        yield "";
      }

      case 3 -> {
        if (tries == 1) {
          yield "Remembering will just bring you pain. Stay with me instead!";
        } else if (tries == 2) {
          yield "Would you leave me behind after all that I have done for you?";
        } else {
          yield "After so many failures you've really done well to make it this far. Let's go back and rest now for awhile.";
        }
      }

      case 4 -> {
        if (tries == 1) {
          yield "Ahh you already found the lighthouse. We can't have you getting ahead of yourself now.";
        } else if (tries == 2) {
          yield "I've always been with you. This lighthouse can't guide you like I do!";
        } else {
          if (maxPagesCollected > playerAttributeContainer.getPagesCollectedCount()) {
            yield "Took you long enough to get back here. Was this glimmer of light really worth it?";
          }

          if (playerAttributeContainer.getPagesCollectedCount() == maxPagesCollected) {
            yield "Ahh, I see... You actually consider perseverance to be a virtue.";
          }

          yield "Really now. Just what is so attractive about that light?";
        }
      }

      case 5 -> "It's not to late to turn ba-";

      default -> "If you're reading this, then the game has a bug!";
    };
  }
}
