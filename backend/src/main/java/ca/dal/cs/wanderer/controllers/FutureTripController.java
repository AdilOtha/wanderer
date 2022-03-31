package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.dtos.FutureTripRequestDto;
import ca.dal.cs.wanderer.exception.GenericResponse;
import ca.dal.cs.wanderer.exception.category.FutureTripNotSaved;
import ca.dal.cs.wanderer.exception.category.PinNotFound;
import ca.dal.cs.wanderer.exception.category.UserNotFound;
import ca.dal.cs.wanderer.models.FutureTrip;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.FutureTripService;
import ca.dal.cs.wanderer.services.UserProfileService;
import ca.dal.cs.wanderer.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wanderer/futuretrip")
public class FutureTripController {

    @Autowired
    private FutureTripService futureTripService;

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/createFutureTrip")
    public ResponseEntity<GenericResponse<FutureTrip>> createFutureTrip(@AuthenticationPrincipal OidcUser principal, @RequestBody FutureTripRequestDto futureTripRequestDto) {
        if(futureTripRequestDto.getPinId() < 0) {
            throw new PinNotFound(ErrorMessages.PINID_NOT_FOUND);
        }

        FutureTrip futureTrip = FutureTripRequestDto.convertToModel(futureTripRequestDto);

        User user = userProfileService.fetchByEmail(principal.getEmail());

        if(user.getId() == null) {
            throw new UserNotFound(ErrorMessages.USERID_NOT_FOUND);
        }

        futureTrip.setUser(user);

        FutureTrip savedFutureTrip = futureTripService.saveFutureTrip(futureTrip);

        if(savedFutureTrip == null) {
            throw new FutureTripNotSaved(ErrorMessages.FUTURETRIP_NOT_SAVED);
        }

        GenericResponse<FutureTrip> futureTripGenericResponse = new GenericResponse<>(true, "Future Trip is saved", savedFutureTrip);
        return new ResponseEntity<>(futureTripGenericResponse, HttpStatus.OK);
    }


    @GetMapping("/fetchFutureTripsByUserId")
    public ResponseEntity<GenericResponse<List<FutureTrip>>> getFutureTripsByUserId(@AuthenticationPrincipal OidcUser principal) {

        List<FutureTrip> futureTrips = futureTripService.fetchFutureTripsByUserId(principal.getEmail());

        GenericResponse<List<FutureTrip>> futureTripsGenericResponse = new GenericResponse<>(true, "Future Trips for requested user fetched successfully", futureTrips);
        return new ResponseEntity<>(futureTripsGenericResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/fetchFutureTripsByPinId")
    public ResponseEntity<GenericResponse<List<FutureTrip>>> getFutureTripsByPinId(@RequestParam(value = "pinId") int pinId) {

        List<FutureTrip> futureTrips = futureTripService.fetchFutureTripsByPinId(pinId);

        GenericResponse<List<FutureTrip>> futureTripsGenericResponse = new GenericResponse<>(true, "Future Trips for requested pin id fetched successfully", futureTrips);
        return new ResponseEntity<>(futureTripsGenericResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteFutureTripById")
    public ResponseEntity deleteFutureTripById(@RequestParam(value = "futureTripId") int futureTripId) {

        futureTripService.deleteFutureTrip(futureTripId);
        GenericResponse<FutureTrip> futureTripGenericResponse = new GenericResponse<>(true, "Future Trip is deleted", null);
        return new ResponseEntity<>(futureTripGenericResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/updateFutureTrip/{futureTripId}")
    public ResponseEntity<GenericResponse<FutureTrip>> updateFutureTripById(@PathVariable(value = "futureTripId") int futureTripId, @RequestBody FutureTripRequestDto futureTripRequestDto) {

        FutureTrip futureTrip = FutureTripRequestDto.convertToModel(futureTripRequestDto);

        FutureTrip updatedFutureTrip = futureTripService.updateFutureTrip(futureTripId, futureTrip);

        GenericResponse<FutureTrip> futureTripGenericResponse = new GenericResponse<>(true, "Future Trip is updated", updatedFutureTrip);
        return new ResponseEntity<>(futureTripGenericResponse, HttpStatus.OK);
    }
}
