package org.greenhouse.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.auth.AuthenticationRequest;
import org.greenhouse.dto.auth.AuthenticationResponse;
import org.greenhouse.dto.auth.RegisterRequest;
import org.greenhouse.service.auth.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация/регистрация")
public class AuthenticationController {

  private final AuthenticationService service;

  @Operation(
      summary = "Регистрация",
      description = "Создание нового аккаунта пользователя")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @Operation(
      summary = "Аутентификация",
      description = "Вход в аккаунт пользователя")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @Operation(
      summary = "Обновление токена",
      description = "Обновление JWT токена пользователя")
//  @CrossOrigin(origins = "${cors-address}")
  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}
