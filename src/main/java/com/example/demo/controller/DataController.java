package com.example.demo.controller;

import com.example.demo.config.RestConfig;
import com.example.demo.model.Data;
import com.example.demo.model.Location;
import com.example.demo.model.Microcontroller;
import com.example.demo.model.RoomHistoryDto;
import com.example.demo.repository.DataRepository;
import com.example.demo.service.DataService;
import com.example.demo.service.LocationService;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
    @Autowired
    private DataRepository dataRepository;

    @Autowired
    RestConfig restConfig;

    @GetMapping("/")
    public List<Data> list() {
        List<Microcontroller> microcontrollers = mcService.listAll();
        Data[] data = new Data[microcontrollers.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Data();
            data[i].setMicrocontroller(mcService.get(microcontrollers.get(i).getId()));
            data[i].setTitle(locationService.get(microcontrollers.get(i).getLocationID()).getName());
        }


        RestTemplate restTemplate = restConfig.restTemplate();

        for (Data d: data) {
            String address = d.getMicrocontroller().getAddress();
            address = "http://"+address+":80";


            try {
                ResponseEntity<String> response = restTemplate.getForEntity(address, String.class);
                if (!response.getStatusCode().isError()) {
                    String responseData = response.getBody();
                    Data dataFromESP = gson.fromJson(responseData, Data.class);
                    d.setId(d.getMicrocontroller().getLocationID());
                    d.setTemperature(dataFromESP.getTemperature());
                    d.setHumidity(dataFromESP.getHumidity());
                    d.setCo2(dataFromESP.getCo2());
                    d.setLight(1024-dataFromESP.getLight());
                    d.setDate_time(new Date());
                    if (d.getMicrocontroller().getLocationID() > 0) {
                        Location loc = locationService.get(d.getMicrocontroller().getLocationID());
                        d.getMicrocontroller().setLocation(loc.getName());
                    }
                } else {
                    System.out.println(response);
                }
            } catch (ResourceAccessException e) {
                System.out.println(e.toString());
            }
        }

        List<Data> dataList = new ArrayList<>(Arrays.stream(data).toList());
        dataList.sort((d1, d2) -> d1.getMicrocontroller().getLocationID() - d2.getMicrocontroller().getLocationID());

        double mint = dataList.get(0).getTemperature();
        double maxt = dataList.get(0).getTemperature();
        double avgt = dataList.get(0).getTemperature();

        double minh = dataList.get(0).getHumidity();
        double maxh = dataList.get(0).getHumidity();
        double avgh = dataList.get(0).getHumidity();

        double minc = dataList.get(0).getCo2();
        double maxc = dataList.get(0).getCo2();
        double avgc = dataList.get(0).getCo2();

        double minl = dataList.get(0).getLight();
        double maxl = dataList.get(0).getLight();
        double avgl = dataList.get(0).getLight();

        int counter = 1;

        List<Data> buildData = new ArrayList<>();
        for (int i = 1; i < dataList.size(); i++) {
            if (dataList.get(i).getMicrocontroller().getLocationID() == dataList.get(i-1).getMicrocontroller().getLocationID()) {
                if (dataList.get(i).getTemperature() > maxt) {
                    maxt = dataList.get(i).getTemperature();
                }
                if (dataList.get(i).getTemperature() < mint) {
                    mint = dataList.get(i).getTemperature();
                }
                avgt += dataList.get(i).getTemperature();


                if (dataList.get(i).getHumidity() > maxh) {
                    maxh = dataList.get(i).getHumidity();
                }
                if (dataList.get(i).getHumidity() < minh) {
                    minh = dataList.get(i).getHumidity();
                }
                avgh += dataList.get(i).getHumidity();


                if (dataList.get(i).getCo2() > maxc) {
                    maxc = dataList.get(i).getCo2();
                }
                if (dataList.get(i).getCo2() < minc) {
                    minc = dataList.get(i).getCo2();
                }
                avgc = dataList.get(i).getCo2();

                if (dataList.get(i).getLight() > maxl) {
                    maxl = dataList.get(i).getLight();
                }
                if (dataList.get(i).getLight() < minl) {
                    minl = dataList.get(i).getLight();
                }
                avgl += dataList.get(i).getLight();

                counter++;
            } else {
                Data d = dataList.get(i-1);
                d.setTemperature(avgt/counter);
                d.setHumidity(avgh/counter);
                d.setCo2(avgc/counter);
                d.setLight(avgl/counter);
                d.setTemperatureRange(mint + "..." + maxt);
                d.setHumidityRange(minh + "..." + maxh);
                d.setCo2Range(minc + "..." + maxc);
                d.setLightRange(minl + "..." + maxl);
                buildData.add(d);
                maxt = dataList.get(i).getTemperature();
                mint = dataList.get(i).getTemperature();
                avgt = dataList.get(i).getTemperature();
                maxh = dataList.get(i).getHumidity();
                minh = dataList.get(i).getHumidity();
                avgh = dataList.get(i).getHumidity();
                maxc = dataList.get(i).getCo2();
                minc = dataList.get(i).getCo2();
                avgc = dataList.get(i).getCo2();
                maxl = dataList.get(i).getLight();
                minl = dataList.get(i).getLight();
                avgl = dataList.get(i).getLight();
                counter = 1;
            }
            if (i == dataList.size()-1) {
                Data d = dataList.get(i);
                d.setTemperature(avgt/counter);
                d.setHumidity(avgh/counter);
                d.setCo2(avgc/counter);
                d.setLight(avgl/counter);
                d.setTemperatureRange(mint + "..." + maxt);
                d.setHumidityRange(minh + "..." + maxh);
                d.setCo2Range(minc + "..." + maxc);
                d.setLightRange(minl + "..." + maxl);
                buildData.add(d);
                maxt = 0;
                mint = 0;
                avgt = 0;
                maxh = 0;
                minh = 0;
                avgh = 0;
                maxc = 0;
                minc = 0;
                avgc = 0;
                maxl = 0;
                minl = 0;
                avgl = 0;
            }
        }
        return buildData;
    }

    @GetMapping("/history")
    public RoomHistoryDto getHistoryMC(@RequestParam int id,
                                   @RequestParam(required = false) String start,
                                   @RequestParam(required = false) String end) {
        RoomHistoryDto dataList = new RoomHistoryDto();
        if (start == null || end == null) {
            dataList = dataService.listByMCID(id);
        } else {
            dataList = dataService.listByMCIDTime(id, start, end);
        }
        return dataList;
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
