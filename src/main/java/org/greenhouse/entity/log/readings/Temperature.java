package org.greenhouse.entity.log.readings;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenhouse.entity.greenhouse.Greenhouses;
import org.greenhouse.entity.sensor.Sensors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "temperature")
public class Temperature extends Readings {

  @ManyToOne
  @JoinColumn(name = "greenhouse_id", nullable = false)
  private Greenhouses greenhouse;

  @ManyToOne
  @JoinColumn(name = "sensor_id", nullable = false)
  private Sensors sensor;
}
