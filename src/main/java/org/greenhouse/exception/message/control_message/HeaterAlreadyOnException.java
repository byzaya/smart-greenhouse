package org.greenhouse.exception.message.control_message;

public class HeaterAlreadyOnException extends RuntimeException {
  public HeaterAlreadyOnException(String message) {
    super(message);
  }
}
