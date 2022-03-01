package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.services.PinUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class PinUpdateController {

    @Autowired
    private PinUpdateService pinUpdateService;

    @PutMapping("/updatePinTimestamp")
    public String sendPinUpdate(){
        try {
            pinUpdateService.sendPinUpdate();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
