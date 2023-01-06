package com.example.demo.controller;

import com.example.demo.model.Data;
import com.example.demo.model.Location;
import com.example.demo.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/")
public class DataController {
    @Autowired
    DataService dataService;

    @GetMapping("/")
    public String list() {
        String address = "http://192.168.0.103:80";
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        try {
            con = (HttpURLConnection) new URL(address).openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } else {
                System.out.println("failed: " + con.getResponseCode() + " error " + con.getResponseMessage());
            }

        } catch (IOException e) {
            System.out.println(e);
            return e.toString();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return sb.toString();
    }

    @GetMapping("/rooms/{id}")
    public Data getRoomData(@PathVariable int id) {
        return generateRoomData(id);
    }

    @GetMapping("/rooms")
    public List<Data> getRoomList() {
        List<Data> list = new ArrayList<>();

        for (int i = 0; i < 5; i++)
            list.add(generateRoomData());

        return list;
    }

    private Data generateRoomData() {
        return generateRoomData(0);
    }

    private Data generateRoomData(int id) {
        if (id == 0)
            id = ThreadLocalRandom.current().nextInt(30, 100);

        String title = "Помещение " + id;

        double temp = ThreadLocalRandom.current().nextDouble(0, 50);
        double h = ThreadLocalRandom.current().nextDouble(30, 100);
        double co2 = ThreadLocalRandom.current().nextDouble(30, 100);
        double light = ThreadLocalRandom.current().nextDouble(100, 4000);

        return new Data(id, title, temp, h, co2, light, new Date());
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
    @PostMapping("/f")
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
