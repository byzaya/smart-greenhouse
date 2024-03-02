package org.greenhouse.entity.sensor;

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
@Table(name = "sensors")
public class Sensors {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive; // активный/неактивный датчик
                            // (если сломан - приходят странные данные,
                            // отличные от данных с других подобных датчиков)

  @Column(name = "sendor_number", nullable = false)
  private Integer sensorNumber; // порядковый номер датчика

  // TODO связь с типом датчика
  // TODO связь с теплицей
}
