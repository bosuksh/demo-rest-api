package me.doflamingo.demorestapi.events.repository;

import me.doflamingo.demorestapi.events.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
