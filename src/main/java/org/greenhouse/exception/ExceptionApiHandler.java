package org.greenhouse.exception;

import org.greenhouse.exception.message.ConfigurationNotFoundException;
import org.greenhouse.exception.message.ControlNotFoundException;
import org.greenhouse.exception.message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.InvalidEmailException;
import org.greenhouse.exception.message.PasswordNotSameException;
import org.greenhouse.exception.message.SeedBedNotFoundException;
import org.greenhouse.exception.message.SensorNotFoundException;
import org.greenhouse.exception.message.SensorTypeNotFoundException;
import org.greenhouse.exception.message.UserNotFoundException;
import org.greenhouse.exception.message.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ConfigurationNotFoundException.class)
  public CustomErrorMessage configurationNotFoundException(ConfigurationNotFoundException e) {
    return new CustomErrorMessage("Configuration for greenhouse is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ControlNotFoundException.class)
  public CustomErrorMessage controlNotFoundException(ControlNotFoundException e) {
    return new CustomErrorMessage("Configuration for seed bed is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(GreenhouseNotFoundException.class)
  public CustomErrorMessage greenhouseNotFoundException(GreenhouseNotFoundException e) {
    return new CustomErrorMessage("Greenhouse is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidEmailException.class)
  public CustomErrorMessage invalidEmailException(InvalidEmailException e) {
    return new CustomErrorMessage("Email is invalid", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(PasswordNotSameException.class)
  public CustomErrorMessage passwordNotSameException(PasswordNotSameException e) {
    return new CustomErrorMessage("Password are not the same", e.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(SeedBedNotFoundException.class)
  public CustomErrorMessage seedBedNotFoundException(SeedBedNotFoundException e) {
    return new CustomErrorMessage("Seed bed is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(SensorNotFoundException.class)
  public CustomErrorMessage sensorNotFoundException(SensorNotFoundException e) {
    return new CustomErrorMessage("Sensor is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(SensorTypeNotFoundException.class)
  public CustomErrorMessage sensorTypeNotFoundException(SensorTypeNotFoundException e) {
    return new CustomErrorMessage("Sensor type is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public CustomErrorMessage userNotFoundException(UserNotFoundException e) {
    return new CustomErrorMessage("User is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WrongPasswordException.class)
  public CustomErrorMessage wrongPasswordException(WrongPasswordException e) {
    return new CustomErrorMessage("Input wrong password", e.getMessage());
  }
}
