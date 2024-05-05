package org.greenhouse.exception.message.control_message;

public class WindowAlreadyFullOpenException extends RuntimeException {
  public WindowAlreadyFullOpenException(String message) {
    super(message);
  }
}
