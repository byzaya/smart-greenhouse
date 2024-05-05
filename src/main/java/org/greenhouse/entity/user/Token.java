package org.greenhouse.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.greenhouse.entity.enums.TokenType;

// TODO в миграциях переделать
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

  @Id @GeneratedValue public Integer id;

  @Column(name = "token", unique = true)
  public String token; // токен

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER; // тип токена

  @Column(name = "revoked")
  public boolean revoked; // аннулирован

  @Column(name = "expired")
  public boolean expired; // истек

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;
}
