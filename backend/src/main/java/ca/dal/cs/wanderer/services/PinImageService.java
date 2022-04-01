package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.PinImages;
import ca.dal.cs.wanderer.repositories.PinImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PinImageService {
    @Autowired
    private PinImageRepo pinImageRepo;

    public PinImages addImage(MultipartFile[] file, Pin savedPin) throws IOException {
        PinImages images = new PinImages(savedPin);
        savedPin.getPinId();
        for (MultipartFile files : file) {
            images.setImage(files.getBytes());
            PinImages image = pinImageRepo.save(images);
        }
        return null;
    }
}
