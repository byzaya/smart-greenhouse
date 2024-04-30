package org.greenhouse.service.greenhouse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.greenhouse.GreenhouseDto;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.log.readings.Light;
import org.greenhouse.exception.message.GreenhouseNotFoundException;
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
    greenhouse.setUser(userRepository.findById(greenhouseDto.user().id()).orElseThrow());
    greenhouse.setConfiguration(configurationRepository.findById(greenhouseDto.configuration().id()).orElseThrow());
    greenhouse.setLight(greenhouseDto.light().stream().map(l -> {
      Light light = new Light();
      light.setGreenhouse(greenhouse);
      // Инициализируйте другие поля light
      return light;
    }).collect(Collectors.toList()));
    // Аналогично для других связанных сущностей

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
  public List<Long> getGreenhouseIdsByUserId(Long userId) {
    return greenhousesRepository.findByUserId(userId).stream()
        .map(Greenhouses::getId)
        .collect(Collectors.toList());
  }
}
