package ca.dal.cs.wanderer.services;

import ca.dal.cs.wanderer.models.PinUpdate;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class PinUpdateService {
    String DOCUMENT_ID = "0dZ4madili5WE9g9iIqn";
    public String sendPinUpdate() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        PinUpdate pinUpdate = new PinUpdate(DOCUMENT_ID, new Date());
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("pin_updates").document(DOCUMENT_ID).set(pinUpdate);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
}
