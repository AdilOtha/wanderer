package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.exception.GenericResponse;
import ca.dal.cs.wanderer.exception.category.EmailNotFound;
import ca.dal.cs.wanderer.exception.category.PrincipalNotFound;
import ca.dal.cs.wanderer.exception.category.pinexception.InvalidCoordinates;
import ca.dal.cs.wanderer.exception.category.pinexception.InvalidPinId;
import ca.dal.cs.wanderer.exception.category.pinexception.PinNotFound;
import ca.dal.cs.wanderer.exception.category.pinexception.PinNotSaved;
import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.repositories.PinRepository;
import ca.dal.cs.wanderer.services.PinService;
import ca.dal.cs.wanderer.services.UserProfileService;
import ca.dal.cs.wanderer.util.ErrorMessages;
import ca.dal.cs.wanderer.util.SuccessMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pin")
public class PinController {

    @Autowired
    private PinService pinService;

    @Autowired
    private UserProfileService userProfileService;

    private final static int MAX_LOCATION_NAME_LENGTH = 50;
    private final static int MAX_DESCRIPTION_LENGTH = 1000;

    @PostMapping("/createPin")
    public ResponseEntity<GenericResponse<Pin>> createPin(@AuthenticationPrincipal OidcUser principal, @RequestPart(value = "pin") Pin pin,
                                                          @RequestPart(value = "images", required = false) MultipartFile[] files) throws Exception {
        User authenticatedUser = getUser(principal);

        if(pin!=null){
            System.out.println("Pin Location Name: " + pin.getLocationName());
        }
        if(pin==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        if(checkInvalidString(pin.getLocationName(), MAX_LOCATION_NAME_LENGTH)){
            throw new Exception(ErrorMessages.INVALID_PIN_LOCATION_NAME.getErrorMessage());
        }
        if(checkInvalidString(pin.getDescription(), MAX_DESCRIPTION_LENGTH)){
            throw new Exception(ErrorMessages.INVALID_PIN_DESCRIPTION.getErrorMessage());
        }

        if(checkInvalidCoordinates(pin.getLatitude(), pin.getLongitude())){
            throw new InvalidCoordinates(ErrorMessages.INVALID_COORDINATES);
        }

        Pin savedPin = pinService.savePin(pin, files, authenticatedUser);

        if(savedPin==null){
            throw new PinNotSaved(ErrorMessages.PIN_NOT_SAVED);
        }
        GenericResponse<Pin> pinGenericResponse = new GenericResponse<>(true, SuccessMessages.PIN_SAVE_SUCCESS.getSuccessMessage(), savedPin);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }

    @GetMapping("/getPinsByRadius")
    public ResponseEntity<GenericResponse<List<PinRepository.PinBasicInfo>>> getPinsByRadius(@AuthenticationPrincipal OidcUser principal, @RequestParam Double radius, @RequestParam Double centerLat, @RequestParam Double centerLng) throws Exception {
        System.out.println(radius + " "+ centerLat + " "+ centerLng);

        getUser(principal);

        if(radius<0){
            throw new Exception(ErrorMessages.INVALID_PIN_RADIUS.getErrorMessage());
        }

        if(checkInvalidCoordinates(centerLat, centerLng)){
            throw new InvalidCoordinates(ErrorMessages.INVALID_COORDINATES);
        }

        List<PinRepository.PinBasicInfo> pinList = pinService.getPinsByRadius(radius, centerLat, centerLng);

        if(pinList==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        GenericResponse<List<PinRepository.PinBasicInfo>> listGenericResponse = new GenericResponse<>(true, SuccessMessages.PINS_RETRIEVE_SUCCESS.getSuccessMessage(), pinList);
        return new ResponseEntity<>(listGenericResponse, HttpStatus.OK);
    }

    @GetMapping("/getPinsByIds")
    public ResponseEntity<GenericResponse<List<Pin>>> getPinsByIds(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer[] pinIds){

        System.out.println(principal);
        getUser(principal);

        List<Pin> pinList = pinService.getPinsByIds(Arrays.asList(pinIds));

        if(pinList==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        GenericResponse<List<Pin>> pinGenericResponse = new GenericResponse<>(true, SuccessMessages.PINS_RETRIEVE_SUCCESS.getSuccessMessage(), pinList);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }

    // get pin by id
    @GetMapping("/getPinById")
    public ResponseEntity<GenericResponse<Pin>> getSinglePinById(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer pinId) throws Exception {
        System.out.println("Pin ID: " + pinId);

        getUser(principal);

        if(pinId<=0){
            throw new Exception(ErrorMessages.INVALID_PIN_ID.getErrorMessage());
        }


        Pin pin = pinService.getSinglePinById(pinId);
        if(pin==null){
            throw new PinNotFound(ErrorMessages.PIN_NOT_FOUND);
        }

        GenericResponse<Pin> pinGenericResponse = new GenericResponse<>(true, SuccessMessages.SINGLE_PIN_RETRIEVE_SUCCESS.getSuccessMessage(), pin);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }

    // delete pin by id
    @DeleteMapping("/deletePinById")
    public ResponseEntity<GenericResponse<Boolean>> deletePinById(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer pinId) throws Exception {
        System.out.println("Pin ID: " + pinId);

        User authenticatedUser = getUser(principal);

        if(pinId<=0){
            throw new InvalidPinId(ErrorMessages.INVALID_PIN_ID);
        }

        Integer userId = authenticatedUser.getId();

        pinService.deletePin(pinId, userId);

        GenericResponse<Boolean> pinGenericResponse = new GenericResponse<>(true, SuccessMessages.PIN_DELETE_SUCCESS.getSuccessMessage(), true);
        return new ResponseEntity<>(pinGenericResponse, HttpStatus.OK);
    }

    private boolean checkInvalidString(String str, int maxLength){
        return str==null || str.trim().isEmpty() || str.length()>maxLength;
    }

    private boolean checkInvalidCoordinates(double latitude, double longitude){
        return latitude<-90 || latitude>90 || longitude<-180 || longitude>180;
    }

    private User getUser(OidcUser principal) {
        if (principal == null) {
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }
        String email = principal.getEmail();
        System.out.println(email);
        if (email == null) {
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }
        return userProfileService.fetchByEmail(email);
    }
}
