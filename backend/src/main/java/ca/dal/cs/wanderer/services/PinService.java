package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.controllers.PinUpdateController;
import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.PinImage;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.repositories.PinImageRepo;
import ca.dal.cs.wanderer.repositories.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PinService {
    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private PinUpdateController pinUpdateController;

    @Autowired
    private PinImageRepo pinImageRepo;

    public Pin savePin(Pin pin, MultipartFile[] files, User user) throws Exception {
        Pin pinCheck;
        if (pin.getPinId() == null || pin.getPinId() <= 0) {
            pinCheck = new Pin(user.getId(), pin.getLocationName(), pin.getLatitude(), pin.getLongitude());
        } else {
            pinCheck = getSinglePinById(pin.getPinId());
            if (pinCheck == null) {
                throw new Exception();
            }
            pinCheck.setLocationName(pin.getLocationName());
            pinCheck.setLatitude(pin.getLatitude());
            pinCheck.setLongitude(pin.getLongitude());
        }
        pinImageRepo.delete(pin.getPinId());
        for (MultipartFile file : files) {
            try {
                PinImage image = new PinImage(file.getBytes());
                pinCheck.addPinImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Pin newPin = pinRepository.save(pinCheck);
        pinUpdateController.sendPinUpdate();
        return newPin;
    }

    public List<Pin> getPinsByRadius(double radius, double centerLat, double centerLng) {
        return pinRepository.getPinsByRadius(radius, centerLat, centerLng);
    }

    public Pin updatePin(Integer pinId, String locationName, Double latitude, Double longitude) {
        Pin existingPin = pinRepository.findById(pinId).orElse(null);
        if (existingPin == null) {
            return null;
        } else {
            if (locationName != null) {
                existingPin.setLocationName(locationName);
            }
            if (latitude != null) {
                existingPin.setLatitude(latitude);
            }
            if (longitude != null) {
                existingPin.setLongitude(longitude);
            }
            Pin updatedPin = pinRepository.save(existingPin);
            pinUpdateController.sendPinUpdate();
            return updatedPin;
        }
    }

    public List<Pin> getPinsByIds(List<Integer> pinIds) {
        return pinRepository.findAllById(pinIds);
    }

    public Pin getSinglePinById(Integer pinId) {
        return pinRepository.findById(pinId).orElse(null);
    }
}
