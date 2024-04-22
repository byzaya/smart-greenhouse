package org.greenhouse.exception.message;

public class WrongPasswordException extends RuntimeException {
  public WrongPasswordException(String message) {
    super(message);
  }
}
