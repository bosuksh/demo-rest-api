package me.doflamingo.demorestapi.event.repository;

import me.doflamingo.demorestapi.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
