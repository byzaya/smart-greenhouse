package org.greenhouse.exception.message;

public class ConfigurationNotFoundException extends RuntimeException {
  public ConfigurationNotFoundException(String message) {
    super(message);
  }
}
