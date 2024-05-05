package org.greenhouse.service;

import lombok.RequiredArgsConstructor;
import org.greenhouse.entity.user.User;
import org.greenhouse.exception.message.UserNotFoundException;
import org.greenhouse.repository.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ValidationService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public User getUserOrThrow(Integer userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
  }
}
