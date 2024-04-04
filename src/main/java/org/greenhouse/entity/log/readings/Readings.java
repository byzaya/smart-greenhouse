package org.greenhouse.entity.log.readings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Readings {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  // TODO receiveLogId

  @Column(name = "value", nullable = false)
  private Integer value; // значение датчика

  // TODO подумать как лучше сделать время
  @Column(name = "receive_time", nullable = false)
  private Timestamp receiveTime; // когда были взяты показания с датчика
}
