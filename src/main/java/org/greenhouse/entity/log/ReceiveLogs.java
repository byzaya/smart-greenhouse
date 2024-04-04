package org.greenhouse.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receive_logs")
public class ReceiveLogs {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "message", nullable = false, length = 255)
  private String message; // информация

  @Column(name = "validity", nullable = false)
  private Boolean validity; // TODO узнать что имеется ввиду

  // TODO подумать как лучше сделать время
  @Column(name = "receive_time", nullable = false)
  private Timestamp receiveTime; // когда были взяты показания с датчиков

  // TODO topicId
}
