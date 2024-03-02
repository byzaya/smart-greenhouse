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
@Table(name = "users")
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "username", nullable = false, unique = true, length = 50)
  private String username; // имя пользователя

  @Column(name = "password", nullable = false, length = 50)
  private byte[] password; // захешированный пароль

  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email; // электронная почта

  // TODO подумать как лучше сделать время
  @Column(name = "created_at", nullable = false)
  private Timestamp createdAt; // когда создан аккаунт

  @Column(name = "updated_at", nullable = false)
  private Timestamp updatedAt; // когда обновлена информация в аккаунте
}
