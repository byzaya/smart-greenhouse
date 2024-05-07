package org.greenhouse.exception;

import org.greenhouse.exception.message.control_message.FanAlreadyOffException;
import org.greenhouse.exception.message.control_message.FanAlreadyOnException;
import org.greenhouse.exception.message.control_message.HeaterAlreadyOffException;
import org.greenhouse.exception.message.control_message.HeaterAlreadyOnException;
import org.greenhouse.exception.message.control_message.LightAlreadyOffException;
import org.greenhouse.exception.message.control_message.LightAlreadyOnException;
import org.greenhouse.exception.message.control_message.WateringAlreadyOffException;
import org.greenhouse.exception.message.control_message.WateringAlreadyOnException;
import org.greenhouse.exception.message.control_message.WindowAlreadyCloseException;
import org.greenhouse.exception.message.control_message.WindowAlreadyFullOpenException;
import org.greenhouse.exception.message.control_message.WindowAlreadyHalfOpenException;
import org.greenhouse.exception.message.not_found_message.ConfigurationNotFoundException;
import org.greenhouse.exception.message.not_found_message.ControlNotFoundException;
import org.greenhouse.exception.message.not_found_message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.InvalidEmailException;
import org.greenhouse.exception.message.IsAutoFalseException;
import org.greenhouse.exception.message.IsAutoTrueException;
import org.greenhouse.exception.message.PasswordNotSameException;
import org.greenhouse.exception.message.not_found_message.SeedBedNotFoundException;
import org.greenhouse.exception.message.not_found_message.SensorNotFoundException;
import org.greenhouse.exception.message.not_found_message.SensorTypeNotFoundException;
import org.greenhouse.exception.message.not_found_message.TopicNotFoundException;
import org.greenhouse.exception.message.not_found_message.UserNotFoundException;
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
    return new CustomErrorMessage(
        "Configuration for greenhouse is not found by id", e.getMessage());
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

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(TopicNotFoundException.class)
  public CustomErrorMessage topicNotFoundException(TopicNotFoundException e) {
    return new CustomErrorMessage("Topic is not found by id", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WrongPasswordException.class)
  public CustomErrorMessage wrongPasswordException(WrongPasswordException e) {
    return new CustomErrorMessage("Input wrong password", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IsAutoFalseException.class)
  public CustomErrorMessage isAutoFalseException(IsAutoFalseException e) {
    return new CustomErrorMessage(
        "Auto configuration is disabled, turn it on to use auto control", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IsAutoTrueException.class)
  public CustomErrorMessage isAutoTrueException(IsAutoTrueException e) {
    return new CustomErrorMessage(
        "Auto configuration is enabled, turn it off to use manual control", e.getMessage());
  }

  // ошибки инфо об устройствах

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(FanAlreadyOffException.class)
  public CustomErrorMessage fanAlreadyOffException(FanAlreadyOffException e) {
    return new CustomErrorMessage(
        "Fan is already off", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(FanAlreadyOnException.class)
  public CustomErrorMessage fanAlreadyOnException(FanAlreadyOnException e) {
    return new CustomErrorMessage(
        "Fan is already on", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HeaterAlreadyOffException.class)
  public CustomErrorMessage heaterAlreadyOffException(HeaterAlreadyOffException e) {
    return new CustomErrorMessage(
        "Heater is already off", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HeaterAlreadyOnException.class)
  public CustomErrorMessage heaterAlreadyOnException(HeaterAlreadyOnException e) {
    return new CustomErrorMessage(
        "Heater is already on", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(LightAlreadyOffException.class)
  public CustomErrorMessage lightAlreadyOffException(LightAlreadyOffException e) {
    return new CustomErrorMessage(
        "Light is already off", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(LightAlreadyOnException.class)
  public CustomErrorMessage lightAlreadyOnException(LightAlreadyOnException e) {
    return new CustomErrorMessage(
        "Light is already on", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WateringAlreadyOffException.class)
  public CustomErrorMessage wateringAlreadyOffException(WateringAlreadyOffException e) {
    return new CustomErrorMessage(
        "Watering is already off", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WateringAlreadyOnException.class)
  public CustomErrorMessage wateringAlreadyOnException(WateringAlreadyOnException e) {
    return new CustomErrorMessage(
        "Watering is already on", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WindowAlreadyCloseException.class)
  public CustomErrorMessage windowAlreadyCloseException(WindowAlreadyCloseException e) {
    return new CustomErrorMessage(
        "Window is already close", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WindowAlreadyFullOpenException.class)
  public CustomErrorMessage windowAlreadyFullOpenException(WindowAlreadyFullOpenException e) {
    return new CustomErrorMessage(
        "Window is already full open", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WindowAlreadyHalfOpenException.class)
  public CustomErrorMessage windowAlreadyHalfOpenException(WindowAlreadyHalfOpenException e) {
    return new CustomErrorMessage(
        "Window is already half open", e.getMessage());
  }
}
