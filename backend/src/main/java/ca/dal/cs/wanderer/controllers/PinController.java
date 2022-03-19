package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.entity.Pin;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.PinService;
import ca.dal.cs.wanderer.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String email = "";

    @PostMapping("/createPin")
    public Pin createPin(@AuthenticationPrincipal OidcUser principal, @RequestBody Pin pin){
        System.out.println(principal);
        if(principal==null){
            return null;
        }
        email = principal.getEmail();
        System.out.println("Authenticated User Email: " + email);
        if(email==null){
            return null;
        }
        User authenticatedUser = userProfileService.fetchByEmail(email);
        Integer userId = Math.toIntExact(authenticatedUser.getId());
        System.out.println("Authenticated User ID: " + userId);
        pin.setUserId(userId);
        return pinService.savePin(pin);
    }

    @GetMapping("/getPinsByRadius")
    public List<Pin> getPinsByRadius(@AuthenticationPrincipal OidcUser principal, @RequestParam Double radius, @RequestParam Double centerLat, @RequestParam Double centerLng){
        System.out.println(radius + " "+ centerLat + " "+ centerLng);
        System.out.println(principal);
        if(principal==null){
            return null;
        }
        email = principal.getEmail();
        System.out.println(email);
        if(email==null){
            return null;
        }

        return pinService.getPinsByRadius(radius, centerLat, centerLng);
    }

    @PutMapping("/updatePin")
    public Pin updatePin(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer pinId, @RequestParam String locationName, @RequestParam Double latitude, @RequestParam Double longitude){
        System.out.println("Pin ID: " + pinId + "Location Name: " + locationName + " Latitude: "+ latitude + " Longitude: " + longitude);
        System.out.println(principal);
        if(principal==null){
            return null;
        }
        email = principal.getEmail();
        System.out.println(email);
        if(email==null){
            return null;
        }

        return pinService.updatePin(pinId, locationName, latitude, longitude);
    }

    @GetMapping("/getPinsByIds")
    public List<Pin> getPinsByIds(@RequestParam Integer[] pinIds){
        return pinService.getPinsByIds(Arrays.asList(pinIds));
    }
}
