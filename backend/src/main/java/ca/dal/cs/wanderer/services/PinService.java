package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.controllers.PinUpdateController;
import ca.dal.cs.wanderer.exception.category.pinexception.PinNotFound;
import ca.dal.cs.wanderer.exception.category.pinexception.PinNotSaved;
import ca.dal.cs.wanderer.exception.category.pinexception.UnauthorizedPinAccess;
import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.PinImage;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.repositories.PinImageRepo;
import ca.dal.cs.wanderer.repositories.PinRepository;
import ca.dal.cs.wanderer.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class PinService {
    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private PinUpdateController pinUpdateController;

    @Autowired
    PinUpdateService pinUpdateService;

    @Autowired
    private PinImageRepo pinImageRepo;

    public Pin savePin(Pin pin, MultipartFile[] files, User user) throws Exception {
        Pin newPin;
        if (pin.getPinId() == null || pin.getPinId() <= 0) {
            newPin = new Pin(user.getId(), pin.getLocationName(), pin.getLatitude(), pin.getLongitude(), pin.getDescription());
        } else {
            newPin = getSinglePinById(pin.getPinId());
            if (newPin == null) {
                throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
            }
            if(!Objects.equals(newPin.getUserId(), user.getId())){
                throw new UnauthorizedPinAccess(ErrorMessages.UNAUTHORIZED_PIN_ACCESS);
            }
            newPin.setLocationName(pin.getLocationName());
            newPin.setLatitude(pin.getLatitude());
            newPin.setLongitude(pin.getLongitude());
            newPin.setDescription(pin.getDescription());
            pinImageRepo.deleteAllByPinId(pin.getPinId());
        }
        List<PinImage> pinImageList = new ArrayList<>();
        if(files != null) {
            for (MultipartFile file : files) {
                try {
                    PinImage image = new PinImage(file.getBytes());
                    image.setPin(newPin);
                    pinImageList.add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(pinImageList.size()>0){
            newPin.setPinImages(pinImageList);
        }
        Pin savedPin = pinRepository.save(newPin);
        pinUpdateService.sendPinUpdate();
        return savedPin;

    }

    public List<PinRepository.PinBasicInfo> getPinsByRadius(double radius, double centerLat, double centerLng){
        return pinRepository.getPinsByRadius(radius, centerLat, centerLng);
    }

    public Pin updatePin(Integer pinId, String locationName, Double latitude, Double longitude) throws ExecutionException, InterruptedException {
        Pin existingPin = pinRepository.findById(pinId).orElse(null);
        if(existingPin == null){
            return null;
        } else {
            if(locationName != null){
                existingPin.setLocationName(locationName);
            }
            if(latitude!=null){
                existingPin.setLatitude(latitude);
            }
            if(longitude!=null){
                existingPin.setLongitude(longitude);
            }
            Pin updatedPin = pinRepository.save(existingPin);
            pinUpdateService.sendPinUpdate();
            return updatedPin;
        }
    }

    public List<Pin> getPinsByIds(List<Integer> pinIds){
        return pinRepository.findAllById(pinIds);
    }

    public Pin getSinglePinById(Integer pinId){
        return pinRepository.findById(pinId).orElse(null);
    }

    // get single by id
    public Pin getSinglePin(Integer pinId){
        return pinRepository.findById(pinId).orElse(null);
    }

    // delete pin by id
    public void deletePin(Integer pinId, Integer userId) throws ExecutionException, InterruptedException {
        Pin pin = getSinglePin(pinId);

        if(pin==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        // check if user is the owner of the pin
        if(!Objects.equals(pin.getUserId(), userId)){
            throw new UnauthorizedPinAccess(ErrorMessages.UNAUTHORIZED_PIN_ACCESS);
        }

        pinRepository.deleteById(pin.getPinId());
        pinUpdateService.sendPinUpdate();
    }

    public Pin getPinById(int pinId) {
        return pinRepository.findById(pinId).get();
    }
}
