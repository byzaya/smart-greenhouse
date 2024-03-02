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
@Table(name = "sensor_type")
public class SensorType {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "sensor_name", nullable = false, length = 50)
  private String sensorName;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "sendor_number", nullable = false)
  private Integer sensorNumber;

  // TODO связь с типом датчика
  // TODO связь с теплицей
}
