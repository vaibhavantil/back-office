package com.hedvig.backoffice.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

  @Id @GeneratedValue private long id;

  @NotNull private String memberId;

  public Subscription(String memberId) {
    this.memberId = memberId;
  }

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "subscription", cascade = CascadeType.REMOVE)
  private Set<ChatContext> chats = new HashSet<>();
}
