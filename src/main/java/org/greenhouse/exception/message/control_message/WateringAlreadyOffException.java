package org.greenhouse.exception.message.control_message;

public class WateringAlreadyOffException extends RuntimeException {
  public WateringAlreadyOffException(String message) {
    super(message);
  }
}
