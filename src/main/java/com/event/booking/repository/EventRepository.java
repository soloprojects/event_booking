package com.event.booking.repository;

import com.event.booking.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT * FROM events e " +
            "WHERE (TRUE = :#{#name == null} OR e.name LIKE %:name%) " +
            "AND (TRUE = :#{#startDate == null} OR e.date >= :startDate) " +
            "AND (TRUE = :#{#endDate == null} OR e.date <= :endDate) " +
            "AND (TRUE = :#{#category == null} OR e.category = :category)", nativeQuery = true)
    List<Event> findEvents(@Param("name") String name,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate,
                           @Param("category") String category);

    List<Event> findEventsByDate(LocalDate date);

}
