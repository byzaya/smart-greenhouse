package org.greenhouse.entity.user;

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
@Table(name = "tokens")
public class Tokens {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "token", nullable = false, length = 255)
  private String token;

  @Column(name = "token_type", nullable = false, length = 50)
  private String tokenType;

  // TODO подумать как лучше сделать время
  @Column(name = "created_at", nullable = false)
  private Timestamp createdAt;

  @Column(name = "expires_at", nullable = false)
  private Timestamp expiresAt;

  // TODO связь с user
}
