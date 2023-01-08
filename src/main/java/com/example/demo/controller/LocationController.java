package com.example.demo.controller;

import com.example.demo.model.Location;
import com.example.demo.service.LocationService;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private MCService mcService;

    Gson gson = new Gson();

    @GetMapping("/locations")
    public List<Location> getLocations() {
        return locationService.list();
    }

    @GetMapping("/locations/{id}")
    public String getLocation(@PathVariable Integer id) {
        return gson.toJson(locationService.get(id));
    }

    @PostMapping("/locations")
    public String createLocation(@RequestBody Location location) {
        locationService.save(location);
        return gson.toJson(location);
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

    @GetMapping("/locations/{id}/mc")
    public String getMCLocation(@PathVariable Integer id) {
        Location loc = locationService.get(id);
        if (loc.getId() == 0) {
            return "";
        }
        return gson.toJson(mcService.findByLocID(id));
    }

}
