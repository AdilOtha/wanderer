package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.Pin;
import ca.dal.cs.wanderer.models.PinImages;
import ca.dal.cs.wanderer.repositories.PinImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PinImageService {
    @Autowired
    private PinImageRepo pinImageRepo;

    public PinImages addImage(MultipartFile[] file, Pin savedPin) throws IOException {
        List<MultipartFile> imageFile=new ArrayList<>();
        PinImages images=new PinImages(savedPin);
        images.setImage(file[1].getBytes());
       // savedPin.setPinImages();
        savedPin.getPinId();

return null;
    }
}
