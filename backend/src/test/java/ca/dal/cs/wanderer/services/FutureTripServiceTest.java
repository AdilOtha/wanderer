package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.FutureTrip;
import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.repositories.FutureTripRepository;
import ca.dal.cs.wanderer.repositories.PinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class FutureTripServiceTest {

    @Mock
    private PinService pinService;

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private FutureTripRepository futureTripRepository;

    @Mock
    private PinRepository pinRepository;

    @InjectMocks
    FutureTripService futureTripService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveFutureTrip() {
        FutureTrip futureTrip = new FutureTrip();
        Pin pin = new Pin();
        pin.setPinId(1);
        futureTrip.setPin(pin);
        futureTrip.setTripName("dummytripname");
        futureTrip.setTripId(1);
        futureTrip.setTripDescription("dummytripdesctiption");


        when(pinService.getPinById(pin.getPinId())).thenReturn(pin);
        when(futureTripRepository.save(futureTrip)).thenReturn(futureTrip);

        FutureTrip savedFutureTrip = futureTripService.saveFutureTrip(futureTrip);

        assertNotNull(savedFutureTrip, "Future Trip saved successfully");

        verify(pinService, times(1)).getPinById(pin.getPinId());
        verify(futureTripRepository, times(1)).save(futureTrip);
    }

    @Test
    public void testFetchFutureTrip() {
        String email ="dummy@gmail.com";

        FutureTrip futureTrip1 = new FutureTrip();

        futureTrip1.setTripName("dummytripname1");
        futureTrip1.setTripId(1);
        futureTrip1.setTripDescription("dummytripdesctiption1");

        FutureTrip futureTrip2 = new FutureTrip();

        futureTrip2.setTripName("dummytripname2");
        futureTrip2.setTripId(2);
        futureTrip2.setTripDescription("dummytripdesctiption1");

        List<FutureTrip> futureTrips = new ArrayList<>();

        futureTrips.add(futureTrip1);
        futureTrips.add(futureTrip2);

        User user = new User();
        user.setEmailId(email);
        user.setFirstName("DummyName");
        user.setLastName("DummyLastName");
        user.setId(1);

        when(userProfileService.fetchByEmail(email)).thenReturn(user);
        when(futureTripRepository.findByUserId(user.getId())).thenReturn(futureTrips);

        List<FutureTrip> futureTripsResponse = futureTripService.fetchFutureTripsByUserId(email);

        assertNotNull(futureTripsResponse, "Future Trips are not null");

        verify(userProfileService, times(1)).fetchByEmail(email);
        verify(futureTripRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    public void testUpdateFutureTrip() {
        Integer futureTripId = 1;

        FutureTrip futureTrip = new FutureTrip();
        futureTrip.setTripName("futureTripName");
        futureTrip.setTripDescription("futureTripDescription");

        FutureTrip updatedFutureTrip = new FutureTrip();

        updatedFutureTrip.setTripName("updatedFutureTripName");
        updatedFutureTrip.setTripDescription("updatedFutureTripDescription");

        when(futureTripRepository.getById(futureTripId)).thenReturn(futureTrip);
        when(futureTripRepository.save(updatedFutureTrip)).thenReturn(updatedFutureTrip);

        FutureTrip updatedFutureTripResponse = futureTripService.updateFutureTrip(futureTripId, updatedFutureTrip);

        assertNotNull(updatedFutureTripResponse, "Future List Updated is Not Null");

        verify(futureTripRepository, times(1)).getById(futureTripId);
        verify(futureTripRepository, times(1)).save(updatedFutureTrip);
    }

    @Test
    public void testFetchFutureTripsByPinId() {
        Integer pinId = 1;

        FutureTrip futureTrip1 = new FutureTrip();

        Pin pin = new Pin();
        pin.setPinId(1);

        futureTrip1.setTripName("dummytripname1");
        futureTrip1.setTripId(1);
        futureTrip1.setTripDescription("dummytripdesctiption1");
        futureTrip1.setPin(pin);

        FutureTrip futureTrip2 = new FutureTrip();

        futureTrip2.setTripName("dummytripname2");
        futureTrip2.setTripId(2);
        futureTrip2.setTripDescription("dummytripdesctiption1");
        futureTrip2.setPin(pin);

        List<FutureTrip> futureTrips = new ArrayList<>();

        futureTrips.add(futureTrip1);
        futureTrips.add(futureTrip2);

        when(futureTripRepository.findByPinPinId(pinId)).thenReturn(futureTrips);

        List<FutureTrip> futureTripsResponse = futureTripService.fetchFutureTripsByPinId(pinId);

        assertNotNull(futureTripsResponse);
        verify(futureTripRepository, times(1)).findByPinPinId(pinId);
    }

    @Test
    public void testDeleteFutureTrip() {
        Integer futureTripId = 1;

        doNothing().when(futureTripRepository).deleteById(futureTripId);

        futureTripService.deleteFutureTrip(futureTripId);

        verify(futureTripRepository, times(1)).deleteById(futureTripId);
    }

}