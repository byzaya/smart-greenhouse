package org.greenhouse.exception.message.control_message;

public class WindowAlreadyCloseException extends RuntimeException {
  public WindowAlreadyCloseException(String message) {
    super(message);
  }
}
