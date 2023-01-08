package com.example.demo.service;

import com.example.demo.model.Data;
import com.example.demo.model.Microcontroller;
import com.example.demo.repository.DataRepository;
import com.example.demo.repository.MCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
@Transactional
public class DataService {
    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MCRepository mcRepository;

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

    public List<Data> listByMCID(int mcID) {
        Microcontroller mc = mcRepository.findById(mcID).get();

        List<Data> dataList = dataRepository.findByMCID(mc);
        return dataList;
    }

    public List<Data> listByMCIDTime(int mcID, String startStr, String endStr) {
        Microcontroller mc = mcRepository.findById(mcID).get();
        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat("yyyy-MM-dd").parse(startStr);
            end = new SimpleDateFormat("yyyy-MM-dd").parse(endStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(end);
            cal.add(Calendar.HOUR_OF_DAY, 23);
            cal.add(Calendar.MINUTE, 59);
            cal.add(Calendar.SECOND, 59);
            end = cal.getTime();
        } catch (ParseException e) {
            System.out.println(e);
        }


        List<Data> dataList = dataRepository.findByMCIDTime(mc, start, end);
        return dataList;
    }
}
