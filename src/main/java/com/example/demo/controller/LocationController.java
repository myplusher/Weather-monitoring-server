package com.example.demo.controller;

import com.example.demo.model.Location;
import com.example.demo.service.LocationService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class LocationController {

    @Autowired
    private LocationService locationService;
    Gson gson = new Gson();

    @GetMapping("/locations")
    public String getLocations() {
        return gson.toJson(locationService.list());
    }

    @GetMapping("/locations/{id}")
    public String getLocation(@PathVariable Integer id) {
        return gson.toJson(locationService.get(id));
    }

    @PutMapping("/locations/{id}")
    public String editLocation(@RequestBody Location location, @PathVariable Integer id) {
        location.setId(id);
        locationService.save(location);
        return gson.toJson("200");
    }

    @DeleteMapping("/locations/{id}")
    public String deleteLocation(@PathVariable Integer id) {
        return gson.toJson("200");
    }

}
