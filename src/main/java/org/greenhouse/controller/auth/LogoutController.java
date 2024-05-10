package org.greenhouse.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.greenhouse.service.auth.LogoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logout")
@RequiredArgsConstructor
@Tag(name = "Выход из аккаунта")
public class LogoutController {

  private final LogoutService logoutService;

  @Operation(summary = "Выход из аккаунта", description = "Выход из аккаунта пользователя")
  @PostMapping
  public ResponseEntity<String> logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    logoutService.logout(request, response, authentication);
    return ResponseEntity.ok("Logged out successfully");
  }
}
