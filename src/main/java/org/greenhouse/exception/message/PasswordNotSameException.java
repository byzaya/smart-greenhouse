package org.greenhouse.exception.message;
public class PasswordNotSameException extends RuntimeException {
  public PasswordNotSameException(String message) {
    super(message);
  }
}
