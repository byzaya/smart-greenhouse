package org.greenhouse.service.greenhouse;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.greenhouse.dto.input.greenhouse.GreenhouseInputDto;
import org.greenhouse.dto.output.greenhouse.GreenhouseDto;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.user.User;
import org.greenhouse.repository.greenhouse.GreenhousesRepository;
import org.greenhouse.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GreenhouseService {

  private final GreenhousesRepository greenhousesRepository;
  private final ValidationService validationService;

  // TODO валидация
  // добавление теплицы
  @Transactional
  public GreenhouseDto createGreenhouse(GreenhouseInputDto greenhouseDto) {
    Greenhouses greenhouse = new Greenhouses();
    greenhouse.setLocation(greenhouseDto.location());
    greenhouse.setGreenhouseName(greenhouseDto.greenhouseName());
    User user = validationService.getUserOrThrow(greenhouseDto.userId());
    greenhouse.setUser(user);
    Greenhouses savedGreenhouse = greenhousesRepository.save(greenhouse);
    return GreenhouseDto.fromGreenhouse(savedGreenhouse);
  }

  // получение инфо о теплице по ее id
  @Transactional(readOnly = true)
  public GreenhouseDto getGreenhouse(Long greenhouseId) {
    Greenhouses greenhouse = validationService.getGreenhouseOrThrow(greenhouseId);
    return GreenhouseDto.fromGreenhouse(greenhouse);
  }

  // получение всех id теплиц по id пользователя
  @Transactional(readOnly = true)
  public List<Long> getGreenhouseIdsByUserId(Integer userId) {
    User user = validationService.getUserOrThrow(userId);
    return user.getGreenhouse().stream().map(Greenhouses::getId).collect(Collectors.toList());
  }

  // удаление теплицы по ее id
  @Transactional
  public void deleteGreenhouse(Long id) {
    Greenhouses greenhouse = validationService.getGreenhouseOrThrow(id);
    greenhousesRepository.delete(greenhouse);
  }
}
