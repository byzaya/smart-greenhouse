package org.greenhouse.entity.greenhouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive; // активна ли эта конфигурация

  @Column(name = "is_auto", nullable = false)
  private Boolean isAuto; // автоматический режим вкл/выкл - если вкл, то программа автоматически поддерживает все параметры
                          // если выкл - пользователь сам управляет параметрами теплицы

  @Column(name = "min_temperature", nullable = false)
  private Integer minTemperature; // минимальная допустимая температура

  @Column(name = "max_temperature", nullable = false)
  private Integer maxTemperature; // максимальная допустимая температура

  @Column(name = "min_light", nullable = false)
  private Integer minLight; // минимальный допустимый уровень освещенности

  @Column(name = "max_light", nullable = false)
  private Integer maxLight; // максимальный допустимый уровень освещенности

  @OneToOne
  @JoinColumn(name = "greenhouse_id", nullable = false, unique = true)
  private Greenhouses greenhouse;
}
