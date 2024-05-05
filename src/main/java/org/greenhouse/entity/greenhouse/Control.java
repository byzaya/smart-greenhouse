package org.greenhouse.entity.greenhouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private Integer windowStatus; // статус открытости окна (закрыто/открыто наполовину/открыто) (0/1/2)

  @Column(name = "light_enabled", nullable = false)
  private Boolean lightEnabled; // вкл/выкл свет

  @Column(name = "fan_enabled", nullable = false)
  private Boolean fanEnabled; // вкл/выкл вентилятор

  @Column(name = "heater_enabled", nullable = false)
  private Boolean heaterEnabled; // вкл/выкл

  @ManyToOne
  @JoinColumn(name = "greenhouse_id", nullable = false)
  private Greenhouses greenhouse;
}
