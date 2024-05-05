package org.greenhouse.exception.message.not_found_message;

public class ConfigurationNotFoundException extends RuntimeException {
  public ConfigurationNotFoundException(String message) {
    super(message);
  }
}
