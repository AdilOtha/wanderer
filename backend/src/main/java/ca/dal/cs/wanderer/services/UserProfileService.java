package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository repo;

    public User fetchByEmail(String email) {

        return repo.findByEmailId(email);
    }

    //this method sets the existing fields to new fields via request params
    public User updateProfile(MultipartFile file, User user, String fName, String lName) throws IOException {
        // user.setBlogImage();

        if (file != null)
            user.setImage(file.getBytes());
        user.setFirstName(fName);
        user.setLastName(lName);
        User savedUser = repo.save(user);
//        user = repo.save(user);
        return savedUser;
    }

    public boolean existsUserByEmailId(String email) {
        return repo.existsUserByEmailIdIgnoreCase(email);
    }

}
