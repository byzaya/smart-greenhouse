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
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

  public CustomErrorMessage configurationNotFoundException(ConfigurationNotFoundException e) {
    return new CustomErrorMessage("Configuration for greenhouse is not found by id", e.getMessage());
  }

  public CustomErrorMessage controlNotFoundException(ControlNotFoundException e) {
    return new CustomErrorMessage("Configuration for seed bed is not found by id", e.getMessage());
  }

  public CustomErrorMessage greenhouseNotFoundException(GreenhouseNotFoundException e) {
    return new CustomErrorMessage("Greenhouse is not found by id", e.getMessage());
  }

  public CustomErrorMessage invalidEmailException(InvalidEmailException e) {
    return new CustomErrorMessage("Email is invalid", e.getMessage());
  }

  public CustomErrorMessage passwordNotSameException(PasswordNotSameException e) {
    return new CustomErrorMessage("Password are not the same", e.getMessage());
  }

  public CustomErrorMessage seedBedNotFoundException(SeedBedNotFoundException e) {
    return new CustomErrorMessage("Seed bed is not found by id", e.getMessage());
  }

  public CustomErrorMessage sensorNotFoundException(SensorNotFoundException e) {
    return new CustomErrorMessage("Sensor is not found by id", e.getMessage());
  }

  public CustomErrorMessage sensorTypeNotFoundException(SensorTypeNotFoundException e) {
    return new CustomErrorMessage("Sensor type is not found by id", e.getMessage());
  }

  public CustomErrorMessage userNotFoundException(UserNotFoundException e) {
    return new CustomErrorMessage("User is not found by id", e.getMessage());
  }

  public CustomErrorMessage wrongPasswordException(WrongPasswordException e) {
    return new CustomErrorMessage("Input wrong password", e.getMessage());
  }
}
