package me.doflamingo.demorestapi.events.domain;

import lombok.*;
import me.doflamingo.demorestapi.accounts.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity @ToString
public class Event {

  @Id @GeneratedValue
  private Integer id;
  private String name;
  private String description;
  private LocalDateTime beginEnrollmentDateTime;
  private LocalDateTime closeEnrollmentDateTime;
  private LocalDateTime beginEventDateTime;
  private LocalDateTime endEventDateTime;
  private String location; //(optional)
  private int basePrice;  //(optional)
  private int maxPrice;   //(optional)
  private int limitOfEnrollment;

  private boolean offline;
  private boolean free;
  @Enumerated(EnumType.STRING)
  private EventStatus eventStatus = EventStatus.DRAFT;

  @ManyToOne
  private Account manager;

  public void update() {
    this.free = (basePrice == 0 && maxPrice == 0);
    this.offline = (location != null && !location.isBlank());
  }
}