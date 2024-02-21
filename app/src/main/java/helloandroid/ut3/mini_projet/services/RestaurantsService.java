package helloandroid.ut3.mini_projet.services;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.mini_projet.DetailsActivity;
import helloandroid.ut3.mini_projet.models.Restaurant;

public class RestaurantsService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CompletableFuture<ArrayList<Restaurant>> getAllRestaurants(){
        ArrayList<Restaurant> resaurants =  new ArrayList<Restaurant>();
        CompletableFuture<ArrayList<Restaurant>> completableFuture = new CompletableFuture<>();

        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, document.getId() + " => " + data);
                                String id = document.getId();
                                String nom = data.get("nom").toString();
                                String address = data.get("address").toString();
                                String horaire = data.get("horaire").toString();
                                float note = Float.parseFloat(data.get("note_moyenne").toString());
                                String description = data.get("description").toString();
                                String type = data.get("type").toString();
                                String photoString = data.get("photos").toString();
                                String[] photos = photoString.substring(1,photoString.length()-1).split(",");
                                GeoPoint coordinates = (GeoPoint) document.getData().get("coordinate");
                                 Restaurant r = new Restaurant(id,nom,address,photos,coordinates,description,horaire,type,note);
                                resaurants.add(r);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            System.out.println("ERROR " +task.getException());

                        }
                        completableFuture.complete(resaurants);
                    }
                });
        return completableFuture;
    }

    public CompletableFuture<Restaurant> getRestaurantById(String restaurantId) {
        CompletableFuture<Restaurant> completableFuture = new CompletableFuture<>();

        db.collection("restaurants")
                .document(restaurantId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                String id = document.getId();
                                String nom = data.get("nom").toString();
                                String address = data.get("address").toString();
                                String horaire = data.get("horaire").toString();
                                float note = Float.parseFloat(data.get("note_moyenne").toString());
                                String description = data.get("description").toString();
                                String type = data.get("type").toString();
                                String photoString = data.get("photos").toString();
                                String[] photos = photoString.substring(1, photoString.length() - 1).split(",");
                                GeoPoint coordinates = (GeoPoint) document.getData().get("coordinate");
                                Restaurant restaurant = new Restaurant(id, nom, address, photos, coordinates, description, horaire, type, note);
                                completableFuture.complete(restaurant);
                            } else {
                                // Restaurant with the provided ID does not exist
                                completableFuture.completeExceptionally(new IllegalArgumentException("No restaurant found with ID: " + restaurantId));
                            }
                        } else {
                            // Error retrieving document
                            completableFuture.completeExceptionally(task.getException());
                        }
                    }
                });

        return completableFuture;
    }

    public Intent goToDetails(Context ctx, Restaurant r){
        Intent intent = new Intent(ctx, DetailsActivity.class);
        intent.putExtra("Titre", r.getNom());
        intent.putExtra("Type", r.getType());
        intent.putExtra("Description", r.getDescription());
        intent.putExtra("Adresse", r.getAddress());
        intent.putExtra("Horaire", r.getHoraire());
        intent.putExtra("Photos", r.getPhotos());
        return intent;
    }
}
