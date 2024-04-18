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
import org.greenhouse.entity.log.readings.Light;
import org.greenhouse.entity.user.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "configurations")
public class Configurations {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "is_active", nullable = false)
  private boolean isActive; // активна ли эта конфигурация

  @Column(name = "is_auto", nullable = false)
  private boolean isAuto; // автоматический режим вкл/выкл - если вкл, то программа автоматически поддерживает все параметры
                          // если выкл - пользователь сам управляет параметрами теплицы

  @Column(name = "min_temperature", nullable = false)
  private int minTemperature; // минимальная допустимая температура

  @Column(name = "max_temperature", nullable = false)
  private int maxTemperature; // максимальная допустимая температура

  @Column(name = "min_light", nullable = false)
  private int minLight; // минимальный допустимый уровень освещенности

  @Column(name = "max_light", nullable = false)
  private int maxLight; // максимальный допустимый уровень освещенности

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL)
  private List<Greenhouses> greenhouse = new ArrayList<>();
}
