package org.greenhouse.entity.greenhouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "configurations")
public class Configurations {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "is_active", nullable = false)
  private boolean isActive; // активна ли эта конфигурация

  @Column(name = "is_auto", nullable = false)
  private boolean isAuto; // автоматический режим вкл/выкл - если вкл, то программа автоматически поддерживает все параметры
                          // если выкл - пользователь сам управляет параметрами теплицы

  @Column(name = "min_temperature", nullable = false)
  private int minTemperature; // минимальная допустимая температура

  @Column(name = "max_temperature", nullable = false)
  private int maxTemperature; // максимальная допустимая температура

  @Column(name = "min_light", nullable = false)
  private int minLight; // минимальный допустимый уровень освещенности

  @Column(name = "max_light", nullable = false)
  private int maxLight; // максимальный допустимый уровень освещенности

  // TODO userId
}
