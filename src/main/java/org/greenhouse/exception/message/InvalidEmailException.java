package org.greenhouse.exception.message;

public class InvalidEmailException extends RuntimeException {
  public InvalidEmailException(String message) {
    super(message);
  }
}
