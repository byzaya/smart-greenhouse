package org.greenhouse.service.user;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.auth.ChangePasswordRequest;
import org.greenhouse.dto.output.user.UserDto;
import org.greenhouse.entity.user.User;
import org.greenhouse.exception.message.PasswordNotSameException;
import org.greenhouse.exception.message.WrongPasswordException;
import org.greenhouse.repository.user.UserRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository repository;
  private final ValidationService validationService;

  // изменение пароля
  public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    // проверяем, что введенный пароль корректный
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new WrongPasswordException("Wrong password");
    }
    // проверка, что оба введенных пароля одинаковые
    if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
      throw new PasswordNotSameException("Password are not the same");
    }
    // обновляем пароль
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    repository.save(user);
  }

  // получение информации о пользователе
  @Transactional(readOnly = true)
  public UserDto getInfo(Integer userId) {
    User user = validationService.getUserOrThrow(userId);
    return UserDto.fromUser(user);
  }
}
