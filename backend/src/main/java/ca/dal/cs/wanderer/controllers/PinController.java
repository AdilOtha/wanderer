package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.entity.Pin;
import ca.dal.cs.wanderer.services.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pin")
public class PinController {
    @Autowired
    PinService pinService;

    @PostMapping("/createPin")
    public Pin createPin(@RequestBody Pin pin){
        return pinService.savePin(pin);
    }

    @GetMapping("/getPinsByRadius")
    public List<Pin> getPinsByRadius(@RequestParam Double radius, @RequestParam Double centerLat, @RequestParam Double centerLng){
        System.out.println(radius + " "+ centerLat + " "+ centerLng);
        List<Pin> pinList = pinService.getPinsByRadius(radius, centerLat, centerLng);
        return pinList;
    }

    @PutMapping("/updatePin")
    public Pin updatePin(@RequestParam Integer pinId, @RequestParam String locationName, @RequestParam Double latitude, @RequestParam Double longitude){
        System.out.println("Pin ID: " + pinId + "Location Name: " + locationName + " Latitude: "+ latitude + " Longitude: " + longitude);
        Pin updatedPin = pinService.updatePin(pinId, locationName, latitude, longitude);
        return updatedPin;
    }
}
