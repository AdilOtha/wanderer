package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.FutureTrip;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.repositories.FutureTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FutureTripService {

    @Autowired
    private FutureTripRepository futureTripRepository;

    @Autowired
    private PinService pinService;

    @Autowired
    private UserProfileService userProfileService;

    public FutureTrip saveFutureTrip(FutureTrip futureTrip) {
        futureTrip.setPin(pinService.getPinById(futureTrip.getPin().getPinId()));
        return futureTripRepository.save(futureTrip);
    }

    public List<FutureTrip> fetchFutureTripsByUserId(String email) {

        User user = userProfileService.fetchByEmail(email);
        return futureTripRepository.findByUserId(user.getId());
    }

    public List<FutureTrip> fetchFutureTripsByPinId(int pinId) {
        return futureTripRepository.findByPinPinId(pinId);
    }

    public FutureTrip updateFutureTrip(int futureTripId, FutureTrip updatedFutureTrip) {
        FutureTrip futureTrip = futureTripRepository.getById(futureTripId);
        futureTrip.setTripName(updatedFutureTrip.getTripName());
        futureTrip.setTripDescription(updatedFutureTrip.getTripDescription());
        futureTrip.setTripDate(updatedFutureTrip.getTripDate());
        return futureTripRepository.save(futureTrip);
    }

    public void deleteFutureTrip(int futureTripId) {
        futureTripRepository.deleteById(futureTripId);
    }
}
