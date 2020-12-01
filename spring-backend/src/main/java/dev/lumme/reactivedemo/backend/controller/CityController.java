package dev.lumme.reactivedemo.backend.controller;

import dev.lumme.reactivedemo.backend.service.CityService;
import dev.lumme.reactivedemo.common.dto.CityDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/cities")
    public List<CityDTO> findCities() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cityService.findCities();
    }
}
