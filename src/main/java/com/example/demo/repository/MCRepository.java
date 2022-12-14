package com.example.demo.repository;

import com.example.demo.model.Microcontroller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MCRepository extends JpaRepository<Microcontroller, Integer> {

    @Transactional
    @Modifying
    @Query("update Microcontroller m set m.address = ?1 where m.id = ?2")
    int update(String address, int id);

}
