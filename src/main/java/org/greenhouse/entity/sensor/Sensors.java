package org.greenhouse.entity.sensor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.log.readings.Humidity;
import org.greenhouse.entity.log.readings.Light;
import org.greenhouse.entity.log.readings.Temperature;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sensors")
public class Sensors {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive; // активный/неактивный датчик
                            // (если сломан - приходят странные данные,
                            // отличные от данных с других подобных датчиков)

  @Column(name = "sensor_number", nullable = false)
  private Integer sensorNumber; // порядковый номер датчика

  @ManyToOne
  @JoinColumn(name = "sensor_type_id", nullable = false)
  private SensorType sensorType;

  @ManyToOne
  @JoinColumn(name = "greenhouse_id", nullable = false)
  private Greenhouses greenhouse;

  @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
  private List<Humidity> humidity = new ArrayList<>();

  @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
  private List<Light> light = new ArrayList<>();

  @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
  private List<Temperature> temperature = new ArrayList<>();
}
