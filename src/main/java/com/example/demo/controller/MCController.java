package com.example.demo.controller;

import com.example.demo.model.Microcontroller;
import com.example.demo.service.MCService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
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

//    @PostMapping("/controllers/{id}")
//    public String editMC(@RequestBody Microcontroller microcontroller, @PathVariable Integer id) {
//        microcontroller.setId(id);
//        mcService.save(microcontroller);
//        Gson gson = new Gson();
//        return gson.toJson("200");
//    }

    @RequestMapping(
            path = "/controllers/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public @ResponseBody Microcontroller authenticate(@RequestBody Microcontroller microcontroller, @PathVariable Integer id) throws Exception {

        microcontroller.setId(id);
        mcService.save(microcontroller);
        Gson gson = new Gson();
        return microcontroller;
    }
}
