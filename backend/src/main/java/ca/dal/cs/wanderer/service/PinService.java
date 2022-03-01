package ca.dal.cs.wanderer.service;

import ca.dal.cs.wanderer.controllers.PinUpdateController;
import ca.dal.cs.wanderer.entity.Pin;
import ca.dal.cs.wanderer.repositories.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PinService {
    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private PinUpdateController pinUpdateController;

    public Pin savePin(Pin pin){
        Pin newPin = pinRepository.save(pin);
        pinUpdateController.sendPinUpdate();
        return newPin;
    }

    public List<Pin> getPinsByRadius(double radius, double centerLat, double centerLng){
        return pinRepository.getPinsByRadius(radius, centerLat, centerLng);
    }
}
