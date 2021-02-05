package me.doflamingo.demorestapi.accounts;


import jdk.jfr.Enabled;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Enabled
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

  @Id @GeneratedValue
  private Integer id;

  private String email;

  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<AccountRole> accountRoles;
}
