package org.greenhouse.entity.sensor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "sensor_name", nullable = false, length = 50)
  private String sensorName; // тип датчика (влажности/освещенности/температуры)

  @OneToMany(mappedBy = "sensorType", cascade = CascadeType.ALL)
  private List<Sensors> sensors = new ArrayList<>();
}
