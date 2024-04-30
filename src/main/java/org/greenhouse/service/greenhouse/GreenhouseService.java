package org.greenhouse.service.greenhouse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.entity.greenhouse.Configurations;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.user.User;
import org.greenhouse.exception.message.ConfigurationNotFoundException;
import org.greenhouse.exception.message.GreenhouseNotFoundException;
import org.greenhouse.exception.message.UserNotFoundException;
import org.greenhouse.repository.greenhouse.ConfigurationsRepository;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreenhouseService {
  private final GreenhousesRepository greenhousesRepository;
  private final UserRepository userRepository;
  private final ConfigurationsRepository configurationRepository;

  // добавление теплицы
  public GreenhouseDto createGreenhouse(GreenhouseDto greenhouseDto) {
    Greenhouses greenhouse = new Greenhouses();
    greenhouse.setLocation(greenhouseDto.location());
    greenhouse.setGreenhouseName(greenhouseDto.greenhouseName());
    Optional<User> user = userRepository.findById(greenhouseDto.user().id());
    if (user.isEmpty()) {
      throw new UserNotFoundException("User not found");
    }
    greenhouse.setUser(user.get());
    Optional<Configurations> configurations =
        configurationRepository.findById(greenhouseDto.configuration().id());
    if (configurations.isEmpty()) {
      throw new ConfigurationNotFoundException("Configuration not found");
    }
    greenhouse.setConfiguration(configurations.get());
    Greenhouses savedGreenhouse = greenhousesRepository.save(greenhouse);
    return GreenhouseDto.fromGreenhouse(savedGreenhouse);
  }

  // получение инфо о теплице по ее id
  public GreenhouseDto getGreenhouse(Long greenhouseId) {
    Optional<Greenhouses> greenhouse = greenhousesRepository.findById(greenhouseId);
    if (greenhouse.isEmpty()) {
      throw new GreenhouseNotFoundException("Greenhouse not found by this id");
    }
    return GreenhouseDto.fromGreenhouse(greenhouse.get());
  }

  // получение всех id теплиц по id пользователя
  public List<Long> getGreenhouseIdsByUserId(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException("User not found");
    }
    return user.get().getGreenhouse().stream().map(Greenhouses::getId).collect(Collectors.toList());
  }

  // удаление теплицы по ее id
  public void deleteGreenhouse(Long id) {
    Optional<Greenhouses> greenhouse = greenhousesRepository.findById(id);
    if (greenhouse.isEmpty()) {
      throw new GreenhouseNotFoundException("Greenhouse not found by this id");
    }
    greenhousesRepository.delete(greenhouse.get());
  }
}
