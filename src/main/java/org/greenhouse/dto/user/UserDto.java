package org.greenhouse.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.greenhouse.entity.user.User;

public record UserDto(@JsonProperty("id") Integer id, @JsonProperty("email") String email) {
  public static UserDto fromUser(User user) {
    return new UserDto(user.getId(), user.getEmail());
  }
}
