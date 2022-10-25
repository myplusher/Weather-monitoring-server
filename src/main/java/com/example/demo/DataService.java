package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class DataService {
    @Autowired
    private DataRepository dataRepository;
    public List<Data> listAllData() {
        return dataRepository.findAll();
    }

    public void saveData(Data data) {
        dataRepository.save(data);
    }

    public Data getData(Integer id) {
        return dataRepository.findById(id).get();
    }

    public void deleteData(Integer id) {
        dataRepository.deleteById(id);
    }
}
