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
@Table(name = "control")
public class Control {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "window_status", nullable = false)
  private int windowStatus; // статус открытости окна (закрыто/открыто наполовину/открыто)

  @Column(name = "watering_enabled", nullable = false)
  private boolean wateringEnabled; // вкл/выкл полив

  @Column(name = "light_enabled", nullable = false)
  private boolean lightEnabled; // вкл/выкл свет

  @Column(name = "fan_enabled", nullable = false)
  private boolean fanEnabled; // вкл/выкл вентилятор

  @Column(name = "heater_enabled", nullable = false)
  private boolean heaterEnabled; // вкл/выкл

  // TODO greenhouseId
}
