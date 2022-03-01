package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.entity.Pin;
import ca.dal.cs.wanderer.service.PinService;
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
    public List<Pin> getPinsByRadius(@RequestParam double radius, @RequestParam double centerLat, @RequestParam double centerLng){
        List<Pin> pinList = pinService.getPinsByRadius(radius, centerLat, centerLng);
        return pinList;
    }
}
