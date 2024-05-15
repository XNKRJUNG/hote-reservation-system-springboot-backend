package edu.miu.cs.cs544.controller;


import edu.miu.cs.cs544.domain.Country;
import edu.miu.cs.cs544.domain.State;
import edu.miu.cs.cs544.service.CountryService;
import edu.miu.cs.cs544.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class CountryAndStateController {

    @Autowired
    CountryService countryService;

    @Autowired
    StateService stateService;

    @PostMapping("/country")
    public ResponseEntity<?> addCountry(@RequestBody Country country) {
        try {
            Country response = countryService.addCountry(country);
            if (response == null) {
                return new ResponseEntity<>("Can't create country", HttpStatus.valueOf(400));
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(400));
        }
    }

    @PutMapping("/country/{countryId}")
    public ResponseEntity<?> updateCountry(@PathVariable String countryId, @RequestBody Country country) throws Exception {
        Country response = countryService.updateCountry(countryId, country);
        if (response == null) {
            return new ResponseEntity<>("Can't Update country", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<?> getCountry(@PathVariable String countryId) {
        Country response = countryService.getCountryById(countryId);
        if (response == null) {
            return new ResponseEntity<>("No country available by this code", HttpStatus.valueOf(400));
        }
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/country")
    public ResponseEntity<?> getCountryAll() {
        List<Country> response = countryService.getAllCountry();
        if (response == null) {
            return new ResponseEntity<>("No country available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/state")
    public ResponseEntity<?> addState(@RequestBody State state) {
        try {
            State response = stateService.addState(state);
            if (response == null) {
                return new ResponseEntity<>("Can't create state", HttpStatus.valueOf(400));
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(400));
        }
    }

    @PutMapping("/state/{stateId}")
    public ResponseEntity<?> updateState(@PathVariable Long stateId, @RequestBody State state) throws Exception {
        State response = stateService.updateState(stateId, state);
        if (response == null) {
            return new ResponseEntity<>("Can't Update state", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<?> getState(@PathVariable Long stateId) {
        State response = stateService.getStateById(stateId);
        if (response == null) {
            return new ResponseEntity<>("No state available by this id", HttpStatus.valueOf(400));
        }
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/state")
    public ResponseEntity<?> getStateAll() {
        List<State> response = stateService.getAllState();
        if (response == null) {
            return new ResponseEntity<>("No state available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
