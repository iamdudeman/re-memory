package technology.sola.engine.rememory.attributes;

import java.util.List;
import java.util.Random;

public class NounDictionary {
  private static final Random random = new Random();

  public static String getRandomNoun(AttributeCategory attributeCategory, List<String> currentNouns) {
    return switch (attributeCategory) {
      case NAME -> NAMES.get(random.nextInt(NAMES.size()));
      case JOB -> JOBS.get(random.nextInt(JOBS.size()));
      case LIKES -> {
        var filteredList = LIKES.stream().filter(like -> !currentNouns.contains(like)).toList();

        yield filteredList.get(random.nextInt(filteredList.size()));
      }
      case INTERESTS -> {
        var filteredList = INTERESTS.stream().filter(like -> !currentNouns.contains(like)).toList();

        yield filteredList.get(random.nextInt(filteredList.size()));
      }
    };
  }

  private static final List<String> NAMES = List.of(
    "Bob",
    "John",
    "Alex"
  );
  private static final List<String> JOBS = List.of(
    "accountant",
    "electrician",
    "web developer"
  );
  private static final List<String> LIKES = List.of(
    "dogs",
    "cats",
    "pizza",
    "tea",
    "coffee"
  );
  private static final List<String> INTERESTS = List.of(
    "programming",
    "game development",
    "sewing",
    "tennis",
    "gaming"
  );
}
