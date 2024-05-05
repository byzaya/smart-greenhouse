package org.greenhouse.resources;

import java.util.regex.Pattern;

public class Patterns {
  public static final String EmailPattern =
      "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
          + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

  public static boolean patternMatches(String string, String regexPattern) {
    return Pattern.compile(regexPattern).matcher(string).matches();
  }
}
