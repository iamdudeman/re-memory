## Bugs

Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: bound must be greater than origin
at java.base/jdk.internal.util.random.RandomSupport.checkRange(RandomSupport.java:203)
at java.base/java.util.random.RandomGenerator.nextFloat(RandomGenerator.java:551)
at technology.sola.rememory.game/technology.sola.engine.rememory.RandomUtils.quickRandomDoubleClamp(RandomUtils.java:23)
at technology.sola.rememory.game/technology.sola.engine.rememory.rooms.ForestRoomWorld.<init>(ForestRoomWorld.java:52)
at technology.sola.rememory.game/technology.sola.engine.rememory.systems.RoomSystem.update(RoomSystem.java:64)


## Pieces missing

* How to win/lose
    * Why collect pages?
    * Why avoid enemies?
* Assets
* Balancing

## New ideas potentially

* Madlibs
* Enemies force bad option word onto you for current slot
* Nouns and adjectives and verbs?
* Complete your madlib
    * madlib randomly selected at start

## Outline

* You can always have a re;memory
    * Who you are
        * attributes that affect gameplay and contribute to your story
            * move speed
            * battery drain
        * re;memory resets all attributes
        * each page pickup as a variant
            * great
            * good -> plus to an attribute
            * bad -> minus to an attribute
            * very bad
        * name (affects all attributes -1 to +1)
        * Where you've been
            * the rooms you have seen so far
            * always start outside the "mansion"
            * first room is always a "dead end"
            * re;memory resets all rooms you have seen
    * What you have
        * start with a flashlight and some battery cells
        * re;memory resets you back to initial
    * When??
    * Why??

### Random notes

* What is on the pages?
    * Story that bridges the pages
    * Each page reveals a bit more and final screen is full story
    * n many pages to finish game
    * 100% chance to spawn page until player picks one up
        * after first page clear all portals to prevent cheese
    * Maybe they are parts of a diary?

* Freeze player and enemies on page acquire

* Enemies hitting you take you back to the forest with a +1 stat boost to make next run a bit easier?
    * page found dialog changes slightly with "retries"

* Sound effects
    * Forget sound effect
    * Enemy hit sound effect (similar to forget one)

--------------------

* Old ideas
    * Attribute "slots"
        * name - 1
        * job - 1
        * interests - 3

    * Battery bar that steadily goes down
    * Maybe only 3 inventory slots
    * Show attributes in top left
        * Haste - move speed
        * Efficiency? - battery goes down slower
        * Clarity? - can see more
        * Luck (better chance to get + attributes)

    * enemies
        * enemy type that steals lapis
        * enemy type that drains battery
        * enemy type that kills
