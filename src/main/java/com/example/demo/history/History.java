package com.example.demo.history;

import com.example.demo.model.Data;
import com.example.demo.model.Location;
import com.example.demo.model.Microcontroller;
import com.example.demo.service.DataService;
import com.example.demo.service.LocationService;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Component
public class History {

    @Autowired
    DataService dataService;

    @Autowired
    MCService mcService;

    @Autowired
    LocationService locationService;

    Gson gson = new Gson();

//    @Scheduled(fixedRate = 900000)
    @Scheduled(fixedRate = 1000*60)
    public void getDataInterval() {
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
                        dataService.saveData(d);
                    }
                } else {
                    System.out.println("failed: " + con.getResponseCode() + " error " + con.getResponseMessage());
                }

            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
        }
    }
}
