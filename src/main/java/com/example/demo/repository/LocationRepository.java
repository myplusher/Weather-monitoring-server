package com.example.demo.repository;

import com.example.demo.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    @Query("SELECT l FROM Location AS l WHERE l.name = ?1")
    Location findByLocationName(String name);
}
