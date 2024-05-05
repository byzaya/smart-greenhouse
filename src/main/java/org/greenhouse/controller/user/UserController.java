package org.greenhouse.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.auth.ChangePasswordRequest;
import org.greenhouse.dto.user.UserDto;
import org.greenhouse.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователь")
public class UserController {

  private final UserService service;

  @Operation(
      summary = "Изменение пароля",
      description = "Изменение пароля пользователя")
  @CrossOrigin(origins = "${cors-address}")
  @PatchMapping
  public ResponseEntity<?> changePassword(
      @RequestBody ChangePasswordRequest request, Principal connectedUser) {
    service.changePassword(request, connectedUser);
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Информация о пользователе",
      description = "Получение информации о пользователе по его id")
  @CrossOrigin(origins = "${cors-address}")
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUserInfo(@PathVariable Integer userId) {
    UserDto user = service.getInfo(userId);
    return ResponseEntity.ok(user);
  }
}
