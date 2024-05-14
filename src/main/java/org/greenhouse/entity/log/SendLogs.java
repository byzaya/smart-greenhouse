package org.greenhouse.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "send_logs")
public class SendLogs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "reply", nullable = false)
  private Boolean reply; // TODO узнать что имеется ввиду

  @Column(name = "command", nullable = false)
  private Integer command; // выполненная команда

  // TODO подумать как лучше сделать время
  @Column(name = "send_time", nullable = false)
  private Timestamp sendTime; // когда была выполнена команда

  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  private Topics topic;
}
