package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.UserProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("wanderer/user")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @Autowired
    private ObjectMapper mapper;

    private String email = "";

    //uncomment this for right output, it wont have google url as of now
/*   @GetMapping("/getDetails")
    public User fetchSingle(@AuthenticationPrincipal OidcUser principal) throws JsonProcessingException {
        email = principal.getEmail();
        String pic= principal.getPicture();
       Object json = mapper.readValue(pic, Object.class);
       String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
       return service.fetchByEmail(email);
    }*/

    @GetMapping("/getDetails")
    public Map<User,String> fetchSingle(@AuthenticationPrincipal OidcUser principal) throws JsonProcessingException {
        HashMap<User,String> map=new HashMap<>();
        email = principal.getEmail();
        String pic= principal.getPicture();
        map.put(service.fetchByEmail(email),pic);
        return map;
    }

    @PutMapping(value = "/updateProfile")
    public String updateUser(@AuthenticationPrincipal OidcUser principal,
                             @RequestParam(value = "file",required = false) MultipartFile file,
                             @RequestParam(value = "First Name",required = false) String fName,
                             @RequestParam(value = "Last Name",required = false) String lName) throws IOException {
        email = principal.getEmail();
        User user= service.fetchByEmail(email);
        if(fName==null)
            fName= principal.getGivenName();
        if(lName==null)
            lName= principal.getFamilyName();
        service.updateProfile(file, user, fName, lName);
        return "Profile updated successfully";
    }

}
