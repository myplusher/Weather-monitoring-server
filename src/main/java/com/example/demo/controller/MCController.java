package com.example.demo.controller;

import com.example.demo.model.Location;
import com.example.demo.model.Microcontroller;
import com.example.demo.service.LocationService;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Контроллер для взаимодействия с микроконтроллерами esp
@RestController
@RequestMapping("/")
public class MCController {

    @Autowired
    MCService mcService;

    @Autowired
    LocationService locationService;

    @GetMapping("/controllers")
    public List<Microcontroller> list() {
        List<Microcontroller> microcontrollers = mcService.listAll();
        for (Microcontroller mc : microcontrollers) {
            if (mc.getLocationID() != 0) {
                Location location = locationService.get(mc.getLocationID());
                mc.setLocation(location.getName());
            }
        }
        return microcontrollers;
    }

    @PostMapping("/controllers")
    public String addMC(@RequestBody Microcontroller microcontroller) {
        Gson gson = new Gson();
        mcService.save(microcontroller);
        return gson.toJson(microcontroller);
    }

    @PutMapping("/controllers/{id}")
    public String editMC(@RequestBody Microcontroller microcontroller, @PathVariable Integer id) {
        Location location = locationService.getByName(microcontroller.getLocation());
        microcontroller.setId(id);
        microcontroller.setLocationID(location.getId());
        mcService.save(microcontroller);
        Gson gson = new Gson();
        return gson.toJson(microcontroller);
    }
}
