package com.example.demo.controller;

import com.example.demo.model.Data;
import com.example.demo.model.Location;
import com.example.demo.model.Microcontroller;
import com.example.demo.service.DataService;
import com.example.demo.service.LocationService;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
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
    
    @Autowired
    MCService mcService;

    @Autowired
    LocationService locationService;

    Gson gson = new Gson();

    @GetMapping("/")
    public Data[] list() {
        List<Microcontroller> microcontrollers = mcService.listAll();
        Data[] data = new Data[microcontrollers.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Data();
            data[i].setMicrocontroller(mcService.get(microcontrollers.get(i).getId()));
        }

        for (Data d: data) {
            String address = d.getMicrocontroller().getAddress();
            address = "http://"+address+":80";
            HttpURLConnection con = null;

            try {
                con = (HttpURLConnection) new URL(address).openConnection();
                con.setRequestMethod("GET");
                con.connect();
                if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String line;
                    while ((line = br.readLine()) != null) {
                        Data dataFromESP = gson.fromJson(line, Data.class);
                        d.setTemperature(dataFromESP.getTemperature());
                        d.setHumidity(dataFromESP.getHumidity());
                        d.setCo2(dataFromESP.getCo2());
                        d.setLight(1024-dataFromESP.getLight());
                        d.setDate_time(new Date());
                        if (d.getMicrocontroller().getLocationID() > 0) {
                            Location loc = locationService.get(d.getMicrocontroller().getLocationID());
                            d.getMicrocontroller().setLocation(loc.getName());
                        }
                    }
                } else {
                    System.out.println("failed: " + con.getResponseCode() + " error " + con.getResponseMessage());
                }

            } catch (IOException e) {
                System.out.println(e);
                return null;
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
        }


        return data;
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

        return new Data(id, title, temp, h, co2, light, new Date(), new Microcontroller());
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
