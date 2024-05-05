package org.greenhouse.exception.message.control_message;

public class HeaterAlreadyOffException extends RuntimeException {
  public HeaterAlreadyOffException(String message) {
    super(message);
  }
}
