package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.exception.GenericResponse;
import ca.dal.cs.wanderer.exception.category.EmailNotFound;
import ca.dal.cs.wanderer.exception.category.PrincipalNotFound;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.UserProfileService;
import ca.dal.cs.wanderer.util.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/wanderer/user")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @Autowired
    private ObjectMapper mapper;

    private String email = "";

    //uncomment this for right output, it wont have google url as of now
//    @GetMapping("/getDetails")
//    public User fetchSingle(@AuthenticationPrincipal OidcUser principal) throws JsonProcessingException {
//        email = principal.getEmail();
////        String pic= principal.getPicture();
////       Object json = mapper.readValue(pic, Object.class);
////       String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//       return service.fetchByEmail(email);
//    }

    @GetMapping("/getDetails")
    public ResponseEntity<GenericResponse<JSONObject>> fetchSingle(@AuthenticationPrincipal OidcUser principal) throws JsonProcessingException {
//        Map<String,String> map=new HashMap<>();
//        System.out.println(principal);
//        email = principal.getEmail();
//        String pic= principal.getPicture();
//        User user = service.fetchByEmail(email);
//        map.put("firstName",user.getFirstName());
//        map.put("lastName",user.getLastName());
//        map.put("emailId", email);
//        map.put("googlePhotoUrl", principal.getPicture());
//        if(user.getImage()!=null){
//            String encodedImage = Base64.getEncoder().encodeToString(user.getImage());
//            map.put("image","data:image/png;base64, "+ encodedImage);
//        } else {
//            map.put("image", null);
//        }
//        return map;
        System.out.println(principal);
        if (principal == null) {
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }

        email = principal.getEmail();

        if (email == null) {
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }

        String googleProfileImage = principal.getPicture();
        User user = service.fetchByEmail(email);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("lastName", user.getLastName());
        jsonObject.put("emailId", email);
        jsonObject.put("googlePhotoUrl", googleProfileImage);
        if (user.getImage() != null) {
            String encodedImage = Base64.getEncoder().encodeToString(user.getImage());
            jsonObject.put("image", "data:image/png;base64, " + encodedImage);
        } else {
            jsonObject.put("image", null);
        }

        GenericResponse<JSONObject> jsonObjectGenericResponse = new GenericResponse<>(true, "User Profile details retrieved successfully", jsonObject);

        return new ResponseEntity<>(jsonObjectGenericResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/updateProfile")
    public ResponseEntity<GenericResponse<User>> updateUser(@AuthenticationPrincipal OidcUser principal,
                                             @RequestParam(value = "image", required = false) MultipartFile file,
                                             @RequestParam(value = "firstName", required = false) String fName,
                                             @RequestParam(value = "lastName", required = false) String lName) throws IOException {
        System.out.println(principal);
        if (principal == null) {
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }

        email = principal.getEmail();

        if (email == null) {
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }

        User user = service.fetchByEmail(email);

        if (fName == null) {
            fName = principal.getGivenName();
        }
        if (lName == null) {
            lName = principal.getFamilyName();
        }
        User savedUser = service.updateProfile(file, user, fName, lName);

//        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("success", "Profile updated successfully");
//        jsonObjectList.add(jsonObject);
//        return new ResponseEntity<>(jsonObjectList, HttpStatus.OK);
        GenericResponse<User> userGenericResponse = new GenericResponse<>(true, "User Profile Updated Successfully", savedUser);

        return new ResponseEntity<>(userGenericResponse, HttpStatus.OK);
    }

}
