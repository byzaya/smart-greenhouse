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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenhouse.entity.log.SendLogs;
import org.greenhouse.entity.log.Topics;
import org.greenhouse.entity.log.readings.Light;
import org.greenhouse.entity.log.readings.Temperature;
import org.greenhouse.entity.sensor.Sensors;
import org.greenhouse.entity.user.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "greenhouse")
public class Greenhouses {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "location", nullable = false, length = 255)
  private String location; // местонахождение теплицы

  @Column(name = "greenhouse_name", nullable = false, length = 50)
  private String greenhouseName; // название теплицы

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToOne(mappedBy = "greenhouse", cascade = CascadeType.ALL, orphanRemoval = true)
  private Configurations configuration;

  @OneToOne(mappedBy = "greenhouse", cascade = CascadeType.ALL, orphanRemoval = true)
  private Control control;

  @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
  private List<Light> light = new ArrayList<>();

  @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
  private List<Temperature> temperature = new ArrayList<>();

  @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
  private List<SeedBeds> seedBeds = new ArrayList<>();

  @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
  private List<Sensors> sensors = new ArrayList<>();

  @OneToMany(mappedBy = "greenhouse", cascade = CascadeType.ALL)
  private List<Topics> topics = new ArrayList<>();
}
