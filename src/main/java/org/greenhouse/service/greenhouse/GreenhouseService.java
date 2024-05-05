package org.greenhouse.service.greenhouse;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.user.User;
import org.greenhouse.exception.message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.UserNotFoundException;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GreenhouseService {
  private final GreenhousesRepository greenhousesRepository;
  private final UserRepository userRepository;

  // TODO валидация
  // добавление теплицы
  @Transactional
  public GreenhouseDto createGreenhouse(GreenhouseDto greenhouseDto) {
    Greenhouses greenhouse = new Greenhouses();
    greenhouse.setLocation(greenhouseDto.location());
    greenhouse.setGreenhouseName(greenhouseDto.greenhouseName());
    User user =
        userRepository
            .findById(greenhouseDto.user().id())
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        "User not found with ID: " + greenhouseDto.user().id()));
    greenhouse.setUser(user);
    Greenhouses savedGreenhouse = greenhousesRepository.save(greenhouse);
    return GreenhouseDto.fromGreenhouse(savedGreenhouse);
  }

  // получение инфо о теплице по ее id
  @Transactional(readOnly = true)
  public GreenhouseDto getGreenhouse(Long greenhouseId) {
    Greenhouses greenhouse =
        greenhousesRepository
            .findById(greenhouseId)
            .orElseThrow(
                () ->
                    new GreenhouseNotFoundException(
                        "Greenhouse not found with ID: " + greenhouseId));
    return GreenhouseDto.fromGreenhouse(greenhouse);
  }

  // получение всех id теплиц по id пользователя
  @Transactional(readOnly = true)
  public List<Long> getGreenhouseIdsByUserId(Integer userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    return user.getGreenhouse().stream().map(Greenhouses::getId).collect(Collectors.toList());
  }

  // удаление теплицы по ее id
  @Transactional
  public void deleteGreenhouse(Long id) {
    Greenhouses greenhouse =
        greenhousesRepository
            .findById(id)
            .orElseThrow(
                () -> new GreenhouseNotFoundException("Greenhouse not found with ID: " + id));
    greenhousesRepository.delete(greenhouse);
  }
}
