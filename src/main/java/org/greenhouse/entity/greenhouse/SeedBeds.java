package org.greenhouse.entity.greenhouse;

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
import org.greenhouse.entity.log.readings.Humidity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seedbeds")
public class SeedBeds {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "seedbed_name", nullable = false, length = 50)
  private String seedbedName; // название/номер грядки

  @Column(name = "is_auto", nullable = false)
  private Boolean
      isAuto; // автоматический режим вкл/выкл - если вкл, то программа автоматически поддерживает
              // все параметры

  // если выкл - пользователь сам управляет параметрами грядки

  @Column(name = "watering_duration", nullable = false)
  private Integer wateringDuration; // длительность полива

  @Column(name = "watering_frequency", nullable = false)
  private Integer wateringFrequency; // частота полива

  @Column(name = "min_humidity", nullable = false)
  private Integer minHumidity; // минимальный уровень влажности

  @Column(name = "max_humidity", nullable = false)
  private Integer maxHumidity; // максимальный уровень влажности

  @ManyToOne
  @JoinColumn(name = "greenhouse_id", nullable = false)
  private Greenhouses greenhouse;

  @OneToMany(mappedBy = "seedBed", cascade = CascadeType.ALL)
  private List<Humidity> humidity = new ArrayList<>();

  @OneToMany(mappedBy = "seedBed", cascade = CascadeType.ALL)
  private List<ControlSeedBed> controlSeedBed = new ArrayList<>();
}
