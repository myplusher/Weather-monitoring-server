package com.example.demo.service;

import com.example.demo.model.Microcontroller;
import com.example.demo.repository.MCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class MCService {

    @Autowired
    private MCRepository mcRepository;
    public List<Microcontroller> listAll() {
        return mcRepository.findAll();
    }

    public void save(Microcontroller data) {
        Microcontroller mc = get(data.getId());
        if (mc.getId() > 0) {
            mcRepository.update(data.getAddress(), data.getId());
        } else {
            mcRepository.save(data);
        }
    }

    public Microcontroller get(Integer id) {
        return mcRepository.findById(id).get();
    }

    public void delete(Integer id) {
        mcRepository.deleteById(id);
    }
}
