package com.example.demo.repository;

import com.example.demo.model.Data;
import com.example.demo.model.Location;
import com.example.demo.model.Microcontroller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DataRepository extends JpaRepository<Data, Integer> {
    @Query("SELECT d FROM Data AS d WHERE d.microcontrollerId = ?1")
    List<Data> findByMCID(Microcontroller mc);

    @Query("SELECT d FROM Data AS d WHERE d.microcontrollerId = ?1 AND d.date_time between ?2 and ?3")
    List<Data> findByMCIDTime(Microcontroller mc, Date start, Date end);
}
