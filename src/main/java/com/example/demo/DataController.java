package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class DataController {
    @Autowired
    DataService dataService;

    @GetMapping("")
    public List<Data> list() {
        return dataService.listAllData();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data> get(@PathVariable Integer id) {
        try {
            Data data = dataService.getData(id);
            return new ResponseEntity<Data>(data, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Data>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/")
    public void add(@RequestBody Data data) {
        //TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 3);
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        data.setDate_time(calendar.getTime());

        dataService.saveData(data);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Data data, @PathVariable Integer id) {
        try {
            Data existUser = dataService.getData(id);
            data.setId(id);
            dataService.saveData(data);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {

        dataService.deleteData(id);
    }
}
