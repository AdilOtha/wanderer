package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.exception.GenericResponse;
import ca.dal.cs.wanderer.exception.category.*;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.PinService;
import ca.dal.cs.wanderer.services.UserProfileService;
import ca.dal.cs.wanderer.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pin")
public class PinController {
    @Autowired
    private PinService pinService;

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/createPin")
    public ResponseEntity<GenericResponse<Pin>> createPin(@AuthenticationPrincipal OidcUser principal, @RequestBody Pin pin) throws Exception {

        if(pin.getLocationName()==null || pin.getLocationName().trim().isEmpty()){
            throw new Exception("Invalid Location Name: Cannot be empty");
        }

        if(pin.getLatitude()<-90 || pin.getLatitude()>90 || pin.getLongitude()<-180 || pin.getLongitude()>180){
            throw new InvalidCoordinates(ErrorMessages.INVALID_COORDINATES);
        }

        System.out.println(principal);
        if(principal==null){
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }
        String email = principal.getEmail();
        System.out.println(email);
        if(email==null){
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }
        User authenticatedUser = userProfileService.fetchByEmail(email);
        Integer userId = authenticatedUser.getId();
        System.out.println("Authenticated User ID: " + userId);
        pin.setUserId(userId);
        Pin savedPin = pinService.savePin(pin);
        if(savedPin==null){
            throw new PinNotSaved(ErrorMessages.PIN_NOT_SAVED);
        }
        GenericResponse<Pin> pinGenericResponse = new GenericResponse<>(true, "Pin saved successfully", savedPin);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }

    @GetMapping("/getPinsByRadius")
    public ResponseEntity<GenericResponse<List<Pin>>> getPinsByRadius(@AuthenticationPrincipal OidcUser principal, @RequestParam Double radius, @RequestParam Double centerLat, @RequestParam Double centerLng) throws Exception {
        System.out.println(radius + " "+ centerLat + " "+ centerLng);

        if(radius<0){
            throw new Exception("Invalid Radius");
        }

        if(centerLat<-90 || centerLat>90 || centerLng<-180 || centerLng>180){
            throw new InvalidCoordinates(ErrorMessages.INVALID_COORDINATES);
        }

        System.out.println(principal);
        if(principal==null){
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }
        String email = principal.getEmail();
        System.out.println(email);
        if(email==null){
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }
        List<Pin> pinList = pinService.getPinsByRadius(radius, centerLat, centerLng);

        if(pinList==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        GenericResponse<List<Pin>> listGenericResponse = new GenericResponse<>(true, "Pins retrieved successfully based on radius", pinList);
        return new ResponseEntity<>(listGenericResponse, HttpStatus.OK);
    }

    @PutMapping("/updatePin")
    public ResponseEntity<GenericResponse<Pin>> updatePin(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer pinId, @RequestParam String locationName, @RequestParam Double latitude, @RequestParam Double longitude) throws Exception {
        System.out.println("Pin ID: " + pinId + "Location Name: " + locationName + " Latitude: "+ latitude + " Longitude: " + longitude);

        if(pinId<0){
            throw new Exception("Invalid Pin ID");
        }

        if(locationName==null || locationName.trim().isEmpty()){
            throw new Exception("Invalid Location Name");
        }

        if(latitude<-90 || latitude>90 || longitude<-180 || longitude>180){
            throw new InvalidCoordinates(ErrorMessages.INVALID_COORDINATES);
        }

        System.out.println(principal);
        if(principal==null){
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }
        String email = principal.getEmail();
        System.out.println(email);
        if(email==null){
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }
        Pin updatedPin = pinService.updatePin(pinId, locationName, latitude, longitude);

        if(updatedPin==null){
            throw new PinNotSaved(ErrorMessages.PIN_NOT_SAVED);
        }

        GenericResponse<Pin> pinGenericResponse = new GenericResponse<>(true, "Pin updated successfully", updatedPin);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }

    @GetMapping("/getPinsByIds")
    public ResponseEntity<GenericResponse<List<Pin>>> getPinsByIds(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer[] pinIds){
        System.out.println(pinIds);

        System.out.println(principal);
        if(principal==null){
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }
        String
                email = principal.getEmail();
        System.out.println(email);
        if(email==null){
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }
        List<Pin> pinList = pinService.getPinsByIds(Arrays.asList(pinIds));

        if(pinList==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        GenericResponse<List<Pin>> pinGenericResponse = new GenericResponse<>(true, "Pins retrieved successfully", pinList);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }
}
