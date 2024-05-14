package org.greenhouse.entity.log;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenhouse.entity.log.readings.Readings;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receive_logs")
public class ReceiveLogs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "message", nullable = false, length = 255)
  private String message; // информация

  @Column(name = "validity", nullable = false)
  private Boolean validity; // TODO узнать что имеется ввиду

  // TODO подумать как лучше сделать время
  @Column(name = "receive_time", nullable = false)
  private Timestamp receiveTime; // когда были взяты показания с датчиков

  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  private Topics topic;

  @OneToMany(mappedBy = "receiveLogs", cascade = CascadeType.ALL)
  private List<Readings> readings = new ArrayList<>();
}
