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

    public void updateProfile(MultipartFile file, User user, String fName, String lName) throws IOException {
        // user.setImage();

        if (file != null)
            user.setImage(file.getBytes());
        user.setFirstName(fName);
        user.setLastName(lName);
        repo.save(user);
    }

    public boolean existsUserByEmailId(String email) {
        return repo.existsUserByEmailIdIgnoreCase(email);
    }

}
