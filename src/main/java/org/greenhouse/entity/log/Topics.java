package org.greenhouse.entity.log;

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
@Table(name = "topics")
public class Topics {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "topic_name", nullable = false, unique = true, length = 50)
  private String topicName; // название темы логов

  @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
  private List<SendLogs> sendLogs = new ArrayList<>();

  @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
  private List<ReceiveLogs> receiveLogs = new ArrayList<>();
}
