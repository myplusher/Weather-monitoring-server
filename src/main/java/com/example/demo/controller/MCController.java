package com.example.demo.controller;

import com.example.demo.model.Microcontroller;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Контроллер для взаимодействия с микроконтроллерами esp
@RestController
@RequestMapping("/")
public class MCController {

    @Autowired
    MCService mcService;

    @GetMapping("/controllers")
    public String list() {
        Gson gson = new Gson();
        return gson.toJson(mcService.listAll());
    }

    @PostMapping("/controllers")
    public String addMC(@RequestBody Microcontroller microcontroller) {
        Gson gson = new Gson();
        mcService.save(microcontroller);
        return gson.toJson("201");
    }

    @PutMapping("/controllers/{id}")
    public String editMC(@RequestBody Microcontroller microcontroller, @PathVariable Integer id) {
        microcontroller.setId(id);
        mcService.save(microcontroller);
        Gson gson = new Gson();
        return gson.toJson("200");
    }
}
