package ca.dal.cs.wanderer.service;

import ca.dal.cs.wanderer.entity.PinUpdate;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class PinUpdateService {
    public String sendPinUpdate() throws ExecutionException, InterruptedException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        PinUpdate pinUpdate;
//        DocumentReference documentReference = dbFirestore.collection("pin_updates").document("0dZ4madili5WE9g9iIqn");
//
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//
//        DocumentSnapshot document = future.get();
//
//        if(document.exists()){
//            pinUpdate = document.toObject(PinUpdate.class);
//            System.out.println(pinUpdate);
//            pinUpdate.setLast_updated(new Date());
//            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("pin_updates").document("0dZ4madili5WE9g9iIqn").set(pinUpdate);
//            return collectionApiFuture.get().getUpdateTime().toString();
//        } else {
//            return "Error while publishing the pin update";
//        }
        Firestore dbFirestore = FirestoreClient.getFirestore();
        PinUpdate pinUpdate = new PinUpdate("0dZ4madili5WE9g9iIqn", new Date());
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("pin_updates").document("0dZ4madili5WE9g9iIqn").set(pinUpdate);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
}
