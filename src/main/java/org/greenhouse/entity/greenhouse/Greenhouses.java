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
@Table(name = "greenhouse")
public class Greenhouses {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "location", nullable = false, length = 255)
  private String location; // местонахождение теплицы

  @Column(name = "greenhouse_name", nullable = false, length = 50)
  private String greenhouseName; // название теплицы

  // TODO userId
  // TODO configurationId
}
