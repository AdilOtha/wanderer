package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.PinRating;
import ca.dal.cs.wanderer.repositories.PinRatingRepository;
import ca.dal.cs.wanderer.repositories.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PinRatingsService {

    @Autowired
    PinRatingRepository pinRatingRepository;

    @Autowired
    PinRepository pinRepository;

    public List<PinRating> getRatings(Integer pinId) {
        return pinRatingRepository.findAllByPinId(pinId);
    }

    public List<PinRating> addRatings(Integer userId, Pin pin, Integer pinRating) {
        PinRating pinRatingObject = pinRatingRepository.findPinRatingByUserIdAndPinId(userId, pin.getPinId());
        if(pinRatingObject == null) {
            pinRatingObject = new PinRating(userId, pinRating);
        } else {
            pinRatingObject.setRating(pinRating);
        }
        pinRatingObject.setPin(pin);
        pin.addPinRating(pinRatingObject);
        pinRepository.save(pin);
        return this.getRatings(pin.getPinId());
    }

    public PinRating getRatingsByID(Integer userId, Integer pinId) {
        return pinRatingRepository.findPinRatingByUserIdAndPinId(userId, pinId);
    }

}